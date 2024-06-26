package com.planner.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.planner.dto.request.team.board.TeamBoardDTO;
import com.planner.dto.request.team.board.TeamBoardListDTO;
import com.planner.mapper.TeamBoardMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamBoardService {

	private final TeamBoardMapper teamBoardMapper;
	
	public int tbCountAll(Long team_id) {
		return teamBoardMapper.teamBoardCountAll(team_id);
	}
	
	public int tbCount(Long team_id, String category, String searchOption, String search) {
		return teamBoardMapper.teamBoardCount(team_id, category, searchOption, search);
	}
	
	public List<TeamBoardListDTO> tblist(Long team_id, String category, String searchOption, String search, int pageNum, int pageSize){
		int start = (pageNum-1)*pageSize + 1;
		int end = pageSize * pageNum;
		return teamBoardMapper.teamBoardList(team_id, category, searchOption, search, start, end);
	}

	public void tbInsert(TeamBoardDTO dto) {
		teamBoardMapper.teamBoardInsert(dto);
	}

	public TeamBoardDTO teamBoardView(Long team_board_id) {
		return teamBoardMapper.teamBoardView(team_board_id);
	}

	public void teamBoardUpdate(TeamBoardDTO dto) {
		teamBoardMapper.teamBoardUpdate(dto);
	}

	public void teamBoardDelete(Long team_board_id) {
		teamBoardMapper.teamBoardDelete(team_board_id);
	}
}
