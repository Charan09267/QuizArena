package com.QuizArenaBackend.contest.service.implementations;

import com.QuizArenaBackend.common.Utility.AuthenticationFacade;
import com.QuizArenaBackend.contest.Exception.ResourceNotFoundException;
import com.QuizArenaBackend.contest.dto.ContestResponse;
import com.QuizArenaBackend.contest.dto.CreateContestRequest;
import com.QuizArenaBackend.contest.dto.LeaderboardResponse;
import com.QuizArenaBackend.contest.dto.UpdateContestRequest;
import com.QuizArenaBackend.contest.entity.Contest;
import com.QuizArenaBackend.contest.entity.ContestAttempt;
import com.QuizArenaBackend.contest.entity.enums.AttemptStatus;
import com.QuizArenaBackend.contest.entity.enums.ContestStatus;
import com.QuizArenaBackend.contest.repository.ContestAttemptRepository;
import com.QuizArenaBackend.contest.repository.ContestQuestionRepository;
import com.QuizArenaBackend.contest.repository.ContestRepository;
import com.QuizArenaBackend.contest.service.Mappers.ContestMapper;
import com.QuizArenaBackend.contest.service.Mappers.LeaderboardMapper;
import com.QuizArenaBackend.contest.service.interfaces.ContestService;
import com.QuizArenaBackend.user.Entity.UserEntity;
import com.QuizArenaBackend.user.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContestServiceImpl implements ContestService {

    private final ContestRepository contestRepository;
    private final ContestQuestionRepository questionRepository;
    private final ContestMapper contestMapper;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final ContestAttemptRepository attemptRepository;
    private final LeaderboardMapper leaderboardMapper;



    @Override
    public ContestResponse createContest(CreateContestRequest request) {

        UserEntity creator = authenticationFacade.getCurrentUser();

        Contest contest = Contest.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .visibility(request.getVisibility())
                .createdBy(creator)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .durationSeconds(request.getDurationSeconds())
                .maxParticipants(request.getMaxParticipants())
                .contestType(request.getContestType())
                .status(ContestStatus.DRAFT)
                .build();

        Contest savedContest = contestRepository.save(contest);

        return contestMapper.toResponse(savedContest);
    }

    @Override
    public ContestResponse getContestById(Long contestId) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contest not found"));

        return contestMapper.toResponse(contest);
    }

    @Override
    public List<ContestResponse> getAllContests() {

        return contestRepository.findAll()
                .stream()
                .map(contestMapper::toResponse)
                .toList();
    }

    @Override
    public ContestResponse updateContest(Long contestId,
                                         UpdateContestRequest request) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contest not found"));

        if (contest.getStatus() == ContestStatus.LIVE ||
                contest.getStatus() == ContestStatus.COMPLETED ||
                contest.getStatus() == ContestStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Contest cannot be updated");
        }

        contest.setTitle(request.getTitle());
        contest.setDescription(request.getDescription());
        contest.setVisibility(request.getVisibility());
        contest.setStartTime(request.getStartTime());
        contest.setEndTime(request.getEndTime());
        contest.setDurationSeconds(request.getDurationSeconds());
        contest.setMaxParticipants(request.getMaxParticipants());
        contest.setContestType(request.getContestType());

        Contest updatedContest = contestRepository.save(contest);

        return contestMapper.toResponse(updatedContest);
    }

    @Override
    public void deleteContest(Long contestId) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contest not found"));

        if (contest.getStatus() != ContestStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only draft contests can be deleted");
        }

        contestRepository.delete(contest);
    }

    @Override
    public ContestResponse publishContest(Long contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow( () -> new ResourceNotFoundException("Contest not found!") );

        if(contest.getStatus() != ContestStatus.DRAFT){
            throw new IllegalStateException("Contest status is not DRAFT , so it may already been published....");
        }

        if (contest.getStartTime().isAfter(contest.getEndTime())) {
            throw new IllegalStateException(
                    "Start time must be before end time");
        }

        long questionCount =
                questionRepository.countByContestId(contestId);

        if(questionCount == 0){
            throw new IllegalStateException(
                    "Contest must contain at least one question"
            );
        }

        contest.setStatus(ContestStatus.UPCOMING);
        contestRepository.save(contest);

        return contestMapper.toResponse(contest);
    }

    @Override
    public ContestResponse cancelContest(Long contestId) {

        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contest not found"));

        if (contest.getStatus() == ContestStatus.LIVE ||
                contest.getStatus() == ContestStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Live or completed contests cannot be cancelled");
        }

        contest.setStatus(ContestStatus.CANCELLED);

        contestRepository.save(contest);

        return contestMapper.toResponse(contest);
    }

    @Override
    @Transactional
    public void activateContests() {
        List<Contest> contests =
                contestRepository.findContestsToActivate(
                        LocalDateTime.now());

        contests.forEach(contest -> {
            contest.setStatus(ContestStatus.LIVE);
            log.info("Contest {} activated", contest.getId());
        });
    }

    @Override
    @Transactional
    public void completeContests() {

        List<Contest> contests =
                contestRepository.findContestsToComplete(
                        LocalDateTime.now());

        contests.forEach(contest -> {
            contest.setStatus(ContestStatus.COMPLETED);
            log.info("Contest {} completed", contest.getId());
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaderboardResponse> getLeaderboard(
            Long contestId) {

        Contest contest =
                contestRepository.findById(contestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Contest not found"));

        List<ContestAttempt> attempts =
                attemptRepository
                        .findLeaderboardByContestId(
                                contest.getId() ,
                                List.of(AttemptStatus.SUBMITTED,  AttemptStatus.AUTO_SUBMITTED)
                        );

        List<LeaderboardResponse> leaderboard =
                new ArrayList<>();

        int rank = 1;

        for (ContestAttempt attempt : attempts) {

            leaderboard.add(
                    leaderboardMapper.toResponse(
                            attempt,
                            rank++)
            );
        }

        return leaderboard;
    }


}
