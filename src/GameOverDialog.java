import javax.swing.JDialog; // 다이얼로그 창
import javax.swing.JLabel; // 라벨
import javax.swing.JPanel; // 패널
import javax.swing.ImageIcon; // 이미지 아이콘
import javax.swing.JComboBox; // 콤보박스

import java.awt.Color; // 색상
import java.awt.Font; // 폰트
import java.awt.Graphics; // 그래픽
import java.awt.Image; // 이미지
import java.awt.BorderLayout; // BorderLayout

public class GameOverDialog extends JDialog { // 게임 오버 다이얼로그 클래스

    private GamePanel gamePanel; // 게임 패널
    private ScorePanel scorePanel; // 점수 패널
    private JLabel scoreLabel = new JLabel(); // 점수 표시 라벨
    private MainPanel mainPanel = new MainPanel(); // 하단 패널
    private JLabel overLa = new JLabel("GAME OVER"); // 게임 오버 라벨
    private int score; // 점수 저장 변수

    private String[] options = {"Easy", "Normal", "Hard"}; // 난이도 옵션
    private String[] modes = {"Practice Mode", "Time Attack", "Score Mode"}; // 게임 모드 옵션
    private JComboBox<String> diffCombo = new JComboBox<>(options); // 난이도 콤보박스
    private JComboBox<String> modeCombo = new JComboBox<>(modes); // 모드 콤보박스

    private int mode; // 게임 모드 인덱스
    private int diff; // 게임 난이도 인덱스

    public GameOverDialog() { // 생성자
        setLayout(new BorderLayout()); // BorderLayout 설정
        setSize(300, 200); // 다이얼로그 크기 설정
        add(new ImagePanel(), BorderLayout.CENTER); // 이미지 패널 중앙 배치
        add(mainPanel, BorderLayout.SOUTH); // 메인 패널 하단 배치
    }

    public void addScore() { // 점수 표시 메서드
        score = scorePanel.getScore(); // 점수 가져오기
        scoreLabel.setText("SCORE: " + Integer.toString(score)); // 점수 텍스트 설정
        scoreLabel.setForeground(Color.RED); // 점수 색상 설정
        scoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 폰트 설정
        mainPanel.revalidate(); // 레이아웃 갱신
        mainPanel.repaint(); // 화면 다시 그리기
    }

    public void setScorePanel(ScorePanel scorePanel) { // 점수 패널 설정
        this.scorePanel = scorePanel; // 점수 패널 저장
    }

    class MainPanel extends JPanel { // 하단 패널 클래스
        public MainPanel() {
            setBackground(new Color(0, 1, 49)); // 배경색 설정
            setLayout(new BorderLayout()); // BorderLayout 설정
            scoreLabel.setHorizontalAlignment(JLabel.CENTER); // 점수 중앙 정렬
            add(scoreLabel, BorderLayout.CENTER); // 점수 라벨 추가
        }
    }

    class ImagePanel extends JPanel { // 이미지 표시 패널
        private ImageIcon icon = new ImageIcon("images/gameOver.png"); // 게임오버 이미지
        private Image img = icon.getImage(); // 이미지 객체

        @Override
        public void paintComponent(Graphics g) { // 그림 그리기
            super.paintComponent(g); // 부모 paint 호출
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // 이미지 출력
        }
    }
}
