package kr.or.ddit.board.service;

import java.util.List;

import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.PaginationInfoVO;

public interface IBoardService {
	public int selectBoardCount(PaginationInfoVO<BoardVO> pagingVO);
	public List<BoardVO> selectBoardList(PaginationInfoVO<BoardVO> pagingVO);
	public BoardVO selectBoard(int boNo);
	public int deleteBoard(int boNo);
	public int insertBoard(BoardVO vo);
	public int updateBoard(BoardVO vo);
}
