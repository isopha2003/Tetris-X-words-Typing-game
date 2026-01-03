import javax.swing.JFrame; // 프레임 클래스
import javax.swing.JLabel; // 라벨 클래스
import javax.swing.JComboBox; // 콤보박스 클래스

import java.awt.Container; // 컨테이너
import java.awt.BorderLayout; // BorderLayout

public class GameFrame extends JFrame { // 게임 메인 프레임 클래스

    private String[] options = {"Easy", "Normal", "Hard"}; // 난이도 옵션
    private String[] modes = {"Time Attack", "Score Mode"}; // 게임 모드 옵션
    private JComboBox<String> diffCombo = new JComboBox<>(options); // 난이도 선택 박스
    private JComboBox<String> modeCombo = new JComboBox<>(modes); // 모드 선택 박스

    private Container contentPane; // 프레임 컨테이너

    private JLabel statusLa = new JLabel("Guest"); // 로그인 상태 라벨

    private MyToolBar toolBar; // 툴바 객체

    private StartDialog startDialog; // 시작 다이얼로그

    private ScorePanel scorePanel = new ScorePanel(); // 점수 패널

    private GameOverDialog gameOverDialog = new GameOverDialog(); // 게임오버 다이얼로그
    private LoginDialog loginDialog; // 로그인 다이얼로그

    private WestSidePanel westPanel = new WestSidePanel(); // 왼쪽 패널

    private GamePanel gamePanel = new GamePanel(scorePanel, gameOverDialog, westPanel); // 게임 패널
    private EastSidePanel eastSidePanel = new EastSidePanel(scorePanel); // 오른쪽 패널

    private MusicPlayer bgm = new MusicPlayer(); // 배경음악 플레이어

    public GameFrame() { // 생성자
        bgm.play("sounds/tetris_start.wav", true); // 시작 배경음악 재생
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료 설정
        contentPane = getContentPane(); // 컨테이너 가져오기

        gamePanel.setBgm(bgm); // 게임 패널에 BGM 전달

        toolBar = new MyToolBar(gamePanel, scorePanel, statusLa); // 툴바 생성
        toolBar.setBgm(bgm); // 툴바에 BGM 전달

        loginDialog = new LoginDialog(this, "Login", toolBar, gamePanel, statusLa); // 로그인 다이얼로그 생성

        toolBar.setDialog(loginDialog); // 툴바에 다이얼로그 설정

        gamePanel.setToolBar(toolBar); // 게임 패널에 툴바 전달

        scorePanel.setGamePanel(gamePanel); // 점수 패널에 게임 패널 전달

        gameOverDialog.setScorePanel(scorePanel); // 게임오버 다이얼로그에 점수 패널 전달

        contentPane.add(toolBar, BorderLayout.NORTH); // 툴바 상단 배치
        contentPane.add(gamePanel, BorderLayout.CENTER); // 게임 패널 중앙 배치
        contentPane.add(eastSidePanel, BorderLayout.EAST); // 오른쪽 패널 배치
        contentPane.add(westPanel, BorderLayout.WEST); // 왼쪽 패널 배치
        contentPane.add(new SouthSidePanel(), BorderLayout.SOUTH); // 하단 패널 배치

        startDialog = new StartDialog(this, true, gamePanel, scorePanel, toolBar, loginDialog, bgm, modeCombo, diffCombo); // 시작 다이얼로그 생성
        startDialog.setLocationRelativeTo(null); // 다이얼로그 중앙 배치
        startDialog.setVisible(true); // 다이얼로그 표시

        setExtendedState(JFrame.MAXIMIZED_BOTH); // 전체 화면 설정
        setVisible(true); // 프레임 표시
    }

    public static void main(String[] args) { // 메인 메서드
        new GameFrame(); // 게임 프레임 실행
    }
}
