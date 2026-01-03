import javax.swing.JLabel; // 라벨 클래스
import javax.swing.JPanel; // 패널 클래스
import javax.swing.JButton; // 버튼 클래스
import javax.swing.JComboBox; // 콤보박스 클래스
import javax.swing.JToolBar; // 툴바 클래스

import java.awt.FlowLayout; // FlowLayout
import java.awt.event.ActionEvent; // 액션 이벤트
import java.awt.event.ActionListener; // 액션 리스너
import java.awt.Color; // 색상 클래스

public class MyToolBar extends JToolBar { // 툴바 클래스
    private LoginDialog loginDialog; // 로그인 다이얼로그
    private JButton startBtn = new JButton("GAME START"); // 게임 시작 버튼
    
    private String[] options = {"Easy", "Normal", "Hard"}; // 난이도 옵션
    private String[] modes = {"Time Attack", "Score Mode"}; // 모드 옵션
    private JComboBox<String> diffCombo = new JComboBox<>(options); // 난이도 콤보박스
    private JComboBox<String> modeCombo = new JComboBox<>(modes); // 모드 콤보박스
    
    private JLabel statusLa; // 상태 라벨
    private JButton loginBtn = new JButton("Login"); // 로그인 버튼
    private GamePanel gamePanel; // 게임 패널
    private ScorePanel scorePanel; // 점수 패널
    private JPanel loginPanel = new JPanel(new FlowLayout()); // 로그인 패널
    private MusicPlayer bgm; // 배경음악 플레이어
    private boolean easterEggs = false; // 이스터 에그 여부
    private EffectSound es = new EffectSound(); // 효과음 객체
    private GaugeLabel gauge; // 게이지 라벨
    
    public MyToolBar(GamePanel gamePanel, ScorePanel scorePanel, JLabel statusLa) { // 생성자
        this.gamePanel = gamePanel; // 게임 패널 설정
        this.scorePanel = scorePanel; // 점수 패널 설정
        this.statusLa = statusLa; // 상태 라벨 설정
        setBackground(new Color(151, 104, 151)); // 툴바 배경색 설정
        
        modeCombo.addActionListener(new ActionListener() { // 모드 선택 시 이벤트
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 버튼 클릭 효과음
            }
        });
        
        diffCombo.addActionListener(new ActionListener() { // 난이도 선택 시 이벤트
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 버튼 클릭 효과음
            }
        });
        
        loginBtn.addActionListener(new ActionListener() { // 로그인 버튼 클릭 시
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 효과음 재생
                loginDialog.setLocationRelativeTo(null); // 다이얼로그 중앙 배치
                loginDialog.setVisible(true); // 로그인 다이얼로그 표시
            }
        });
        
        startBtn.setBackground(new Color(154, 154, 154)); // 시작 버튼 배경색
        startBtn.setForeground(new Color(33, 33, 71)); // 시작 버튼 글자색
        startBtn.addActionListener(new ActionListener() { // 시작 버튼 클릭 시
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 효과음 재생
                gameStart(); // 게임 시작 메서드 호출
            }
        });
        
        add(startBtn); // 시작 버튼 추가
        add(new JLabel("Mode")); // 모드 라벨 추가
        add(modeCombo); // 모드 콤보박스 추가
        add(new JLabel("Difficulty")); // 난이도 라벨 추가
        add(diffCombo); // 난이도 콤보박스 추가
        
        loginPanel.setBackground(new Color(151, 104, 151)); // 로그인 패널 배경색
        loginPanel.add(statusLa); // 상태 라벨 추가
        loginPanel.add(loginBtn); // 로그인 버튼 추가

        add(loginPanel); // 로그인 패널 툴바에 추가
    }
    
    public void gameStart() { // 게임 시작 메서드
        bgm.stop(); // 현재 음악 정지
        if (easterEggs == true) { // 이스터 에그 체크
            bgm.play("sounds/easterEggsBgm.wav", true); // 이스터 에그 음악 재생
        } else {
            bgm.play("sounds/tetris1.wav", true); // 기본 배경음악 재생
        }
        int mode = modeCombo.getSelectedIndex(); // 선택된 게임 모드
        int diff = diffCombo.getSelectedIndex(); // 선택된 난이도
        gamePanel.reset(); // 게임 패널 초기화
        scorePanel.reset(); // 점수 패널 초기화
        scorePanel.setDiff(diff); // 점수 패널 난이도 설정
        scorePanel.setMode(mode); // 점수 패널 모드 설정
        scorePanel.revalidate(); // 레이아웃 갱신
        scorePanel.repaint(); // 화면 갱신
        gamePanel.start(mode, diff); // 게임 시작
        gamePanel.getFocus(); // 게임 패널 포커스
    }
    
    public void setEasterEggs(boolean easterEggs) { // 이스터 에그 설정
        this.easterEggs = easterEggs; // 필드 값 저장
    }
    
    public void setDialog(LoginDialog loginDialog) { // 로그인 다이얼로그 설정
        this.loginDialog = loginDialog; // 다이얼로그 저장
    }
    
    public void setBgm(MusicPlayer bgm) { // 배경음악 설정
        this.bgm = bgm; // 음악 플레이어 저장
    }
    
    public void removeLoginBtn() { // 로그인 버튼 제거
        loginPanel.remove(loginBtn); // 로그인 버튼 제거
        loginPanel.revalidate(); // 레이아웃 갱신
        loginPanel.repaint(); // 화면 갱신
    }
    
    public void removeLogoutBtn(JButton btn) { // 로그아웃 버튼 제거
        loginPanel.remove(btn); // 버튼 제거
        loginPanel.revalidate(); // 레이아웃 갱신
        loginPanel.repaint(); // 화면 갱신
    }
    
    public void addLoginBtn() { // 로그인 버튼 추가
        loginPanel.add(loginBtn); // 로그인 버튼 추가
        loginPanel.revalidate(); // 레이아웃 갱신
        loginPanel.repaint(); // 화면 갱신
    }
    
    public void addLogoutBtn(JButton btn) { // 로그아웃 버튼 추가
        loginPanel.add(btn); // 버튼 추가
        loginPanel.revalidate(); // 레이아웃 갱신
        loginPanel.repaint(); // 화면 갱신
    }
}
