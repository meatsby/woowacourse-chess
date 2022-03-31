package chess.domain.state;

import chess.domain.board.Rank;
import chess.domain.piece.Blank;
import chess.domain.piece.Direction;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import java.util.Map;

public abstract class Playing extends GameStarted {

    private static final String INVALID_MOVEMENT_EXCEPTION_MESSAGE = "이동이 불가능한 위치입니다.";
    private static final String OBSTACLE_EXCEPTION_MESSAGE = "경로에 기물이 존재합니다.";
    static final String IS_NOT_YOUR_TURN_EXCEPTION_MESSAGE = "본인의 기물이 아닙니다.";

    public Playing(Map<Integer, Rank> ranks) {
        super(ranks);
    }

    GameState movePiece(Position start, Position target) {
        Piece selected = getPiece(start);

        if (selected.isKnight()) {
            return jump(start, target);
        }
        return moveStraight(start, target);
    }

    private GameState jump(Position start, Position target) {
        return executeMove(start, target);
    }

    private GameState executeMove(Position start, Position target) {
        Piece selected = getPiece(start);
        Piece targetPiece = getPiece(target);

        if (selected.isMovable(targetPiece)) {
            return updateBoard(target, selected, start, targetPiece);
        }

        throw new IllegalArgumentException(INVALID_MOVEMENT_EXCEPTION_MESSAGE);
    }

    private GameState moveStraight(Position start, Position target) {
        checkPath(start, target, Direction.findDirection(start, target));
        return executeMove(start, target);
    }

    private void checkPath(Position start, Position target, Direction direction) {
        if (start.calculateStraightDistance(target) == 1) {
            return;
        }
        Position afterStartTarget = start.createNextPosition(direction);

        for (Position position = afterStartTarget;
             !position.equals(target);
             position = position.createNextPosition(direction)) {
            validateCollision(position);
        }
    }

    private void validateCollision(Position position) {
        if (!getPiece(position).isBlank()) {
            throw new IllegalArgumentException(OBSTACLE_EXCEPTION_MESSAGE);
        }
    }

    private GameState updateBoard(Position target, Piece selected, Position start, Piece targetPiece) {
        updatePiece(target, selected);
        updatePiece(start, new Blank(start));
        selected.updatePosition(targetPiece.getPosition());
        return judgeStatus(targetPiece);
    }

    private void updatePiece(Position position, Piece piece) {
        ranks.get(position.getY())
                .getPieces()
                .set(position.getX(), piece);
    }

    private GameStarted judgeStatus(Piece targetPiece) {
        if (targetPiece.isKing()) {
            return judgeWinner();
        }
        return judgeTurn();
    }

    Piece getPiece(Position position) {
        return ranks.get(position.getY())
                .getPieces()
                .get(position.getX());
    }

    @Override
    public boolean isEnd() {
        return false;
    }

    @Override
    public Winner findWinner() {
        throw new IllegalStateException("게임이 아직 진행 중 입니다.");
    }

    @Override
    public GameState terminate() {
        return new Terminate(ranks);
    }

    @Override
    public double calculateBlackScore() {
        throw new IllegalStateException("아직 게임이 끝나지 않아 점수를 계산할 수 없습니다.");
    }

    @Override
    public double calculateWhiteScore() {
        throw new IllegalStateException("아직 게임이 끝나지 않아 점수를 계산할 수 없습니다.");
    }
}
