package com.QuizArenaBackend.contest.service;

import com.QuizArenaBackend.contest.dto.ContestResponse;
import com.QuizArenaBackend.contest.dto.CreateContestRequest;
import com.QuizArenaBackend.contest.dto.UpdateContestRequest;
import com.QuizArenaBackend.contest.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestServiceImpl implements ContestService {

    private final ContestRepository contestRepository;

    @Override
    public ContestResponse createContest(CreateContestRequest request) {
        // implementation
        return null;
    }

    @Override
    public ContestResponse getContestById(Long contestId) {
        // implementation
        return null;
    }

    @Override
    public List<ContestResponse> getAllContests() {
        // implementation
        return null;
    }

    @Override
    public ContestResponse updateContest(
            Long contestId,
            UpdateContestRequest request) {
        // implementation
        return null;
    }

    @Override
    public void deleteContest(Long contestId) {
        // implementation

    }

}
