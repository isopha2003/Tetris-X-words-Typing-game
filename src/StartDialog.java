import javax.swing.JDialog; // JDialog 임포트
import javax.swing.JFrame; // JFrame 임포트
import javax.swing.JLabel; // JLabel 임포트
import javax.swing.JOptionPane; // JOptionPane 임포트
import javax.swing.JPanel; // JPanel 임포트
import javax.swing.JTextField; // JTextField 임포트
import javax.swing.SwingUtilities; // SwingUtilities 임포트
import javax.swing.UIManager; // UIManager 임포트
import javax.swing.ImageIcon; // ImageIcon 임포트
import javax.swing.JButton; // JButton 임포트
import javax.swing.JComboBox; // JComboBox 임포트

import java.awt.Color; // Color 임포트
import java.awt.Dimension; // Dimension 임포트
import java.awt.Font; // Font 임포트
import java.awt.Graphics; // Graphics 임포트
import java.awt.Image; // Image 임포트
import java.awt.event.ActionEvent; // ActionEvent 임포트
import java.awt.event.ActionListener; // ActionListener 임포트

public class StartDialog extends JDialog { // StartDialog 클래스 정의
    private JLabel title = new JLabel("Tetris X Word Typing Game"); // 타이틀 라벨 생성
    
    private LoginDialog loginDialog; // 로그인 다이얼로그
    
    private GamePanel gamePanel; // 게임 패널
    private ScorePanel scorePanel; // 점수 패널
    
    private MyToolBar toolBar; // 툴바
    
    private String user = null; // 사용자 이름 저장
    
    private JButton startBtn = new JButton("GAME START"); // 게임 시작 버튼

    private JComboBox<String> diffCombo; // 난이도 콤보박스
    private JComboBox<String> modeCombo; // 모드 콤보박스
    private JLabel statusLa = new JLabel("Guest"); // 상태 라벨
    private JButton loginBtn = new JButton("Login"); // 로그인 버튼
    
    private MusicPlayer bgm; // 배경음악 재생 객체
    
    private boolean easterEggs = false; // 이스터에그 활성화 여부
    
    private EffectSound es = new EffectSound(); // 효과음 재생 객체
    
    private JTextField hiddenTF = new JTextField(); // 보이지 않는 텍스트 필드

    public StartDialog(JFrame frame, boolean isModal, GamePanel gamePanel, ScorePanel scorePanel, MyToolBar toolBar,
            LoginDialog loginDialog, MusicPlayer bgm, JComboBox<String> modeCombo, JComboBox<String> diffCombo) { // 생성자
        super(frame, isModal); // 부모 생성자 호출
        this.gamePanel = gamePanel; // 게임 패널 저장
        this.scorePanel = scorePanel; // 점수 패널 저장
        this.toolBar = toolBar; // 툴바 저장
        this.loginDialog = loginDialog; // 로그인 다이얼로그 저장
        this.bgm = bgm; // 배경음악 저장
        this.modeCombo = modeCombo; // 모드 콤보박스 저장
        this.diffCombo = diffCombo; // 난이도 콤보박스 저장
        
        setSize(700, 500); // 다이얼로그 크기 설정
        setLocationRelativeTo(frame); // 화면 중앙 위치
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // 종료 버튼 무시

        addWindowListener(new java.awt.event.WindowAdapter() { // 창 닫기 이벤트
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) { // 창 닫기
                System.exit(0); // 프로그램 전체 종료
            }
        });

        hiddenTF.setOpaque(false); // 배경 투명
        hiddenTF.setBorder(null); // 테두리 제거
        hiddenTF.setForeground(new Color(0,0,0,0)); // 글자 투명
        hiddenTF.setCaretColor(new Color(0,0,0,0)); // 커서 투명
        hiddenTF.setFocusable(true); // 포커스 가능
        hiddenTF.requestFocus(); // 포커스 요청
        
        hiddenTF.addActionListener(new ActionListener() { // Enter 입력 이벤트
            @Override
            public void actionPerformed(ActionEvent e) { // 이벤트 처리
                String inputWord = hiddenTF.getText(); // 입력 단어 가져오기
                if (inputWord.equals("하이솝")) { // 특정 단어 체크
                    bgm.stop(); // 음악 중지
                    bgm.play("sounds/tetris2.wav", true); // 음악 재생
                    easterEggs = true; // 이스터에그 활성화
                    gamePanel.setEasterEggs(easterEggs); // 게임 패널 설정
                    toolBar.setEasterEggs(easterEggs); // 툴바 설정
                    scorePanel.setEasterEggs(easterEggs); // 점수판 설정
                    hiddenTF.setText(""); // 입력 초기화
                    return;
                }
            }
        });
        
        ImagePanel background = new ImagePanel("images/tetris.png"); // 배경 이미지 패널
        background.setLayout(null); // 절대 좌표 사용
        setContentPane(background); // 배경 패널 설정
        background.add(hiddenTF); // 텍스트 필드 추가
        hiddenTF.setBounds(0, 0, 1, 1); // 안 보이게 위치 조정
        
        startBtn.addActionListener(new ActionListener() { // 게임 시작 버튼 이벤트
            @Override
            public void actionPerformed(ActionEvent e) { // 이벤트 처리
                es.play("sounds/buttonBgm.wav", +6.02f); // 효과음 재생
                if(user == null) { // 로그인 안한 상태
                    UIManager.put("OptionPane.yesButtonText", "Yes"); // 예 버튼 텍스트
                    UIManager.put("OptionPane.noButtonText", "No"); // 아니오 버튼 텍스트
                    int result = JOptionPane.showConfirmDialog(null, "If you don't log in, your score will not be saved. Do you want to continue?", "Confirm", JOptionPane.YES_NO_OPTION); // 확인 대화상자
                    if(result == JOptionPane.CLOSED_OPTION || result == JOptionPane.NO_OPTION) { // 취소 또는 아니오
                    }
                    else if(result == JOptionPane.YES_OPTION) { // 예
                        es.play("sounds/buttonBgm.wav", +6.02f); // 효과음
                        setVisible(false); // 창 숨기기
                        gameStart(); // 게임 시작
                    }
                }
                else { // 로그인 상태
                    es.play("sounds/buttonBgm.wav", +6.02f); // 효과음
                    setVisible(false); // 창 숨기기
                    gameStart(); // 게임 시작
                    SwingUtilities.invokeLater(() -> gamePanel.getFocus()); // 게임 패널 포커스
                }
            }
        });
        
        loginBtn.addActionListener(new ActionListener() { // 로그인 버튼 이벤트
            @Override
            public void actionPerformed(ActionEvent e) { // 이벤트 처리
                es.play("sounds/buttonBgm.wav", +6.02f); // 효과음
                user = loginDialog.getContinueDialog().getId(); // 사용자 ID 가져오기
                loginDialog.setLocationRelativeTo(StartDialog.this); // 위치 설정
                loginDialog.setVisible(true); // 다이얼로그 표시
            }
        });
        
        modeCombo.addActionListener(new ActionListener() { // 모드 콤보박스 이벤트
            @Override
            public void actionPerformed(ActionEvent e) { // 이벤트 처리
                es.play("sounds/buttonBgm.wav", +6.02f); // 효과음
            }
        });
        
        diffCombo.addActionListener(new ActionListener() { // 난이도 콤보박스 이벤트
            @Override
            public void actionPerformed(ActionEvent e) { // 이벤트 처리
                es.play("sounds/buttonBgm.wav", +6.02f); // 효과음
            }
        });
        
        title.setFont(new Font("맑은 고딕", Font.BOLD, 32)); // 제목 폰트 설정
        title.setForeground(Color.ORANGE); // 제목 색상 설정

        Dimension titleSize = title.getPreferredSize(); // 크기 계산
        title.setSize(titleSize); // 크기 적용

        int x = (700 - titleSize.width) / 2; // 중앙 X 좌표
        int y = 60; // Y 좌표

        title.setLocation(x, y); // 위치 설정
        background.add(title); // 배경에 추가

        startBtn.setBounds(200, 350, 300, 60); // 시작 버튼 위치
        statusLa.setBounds(50, 50, 100, 30); // 상태 라벨 위치
        loginBtn.setBounds(50, 90, 100, 30); // 로그인 버튼 위치
        modeCombo.setBounds(50, 150, 150, 30); // 모드 콤보박스 위치
        diffCombo.setBounds(50, 200, 150, 30); // 난이도 콤보박스 위치

        background.add(startBtn); // 배경에 버튼 추가
        background.add(statusLa); // 상태 라벨 추가
        background.add(loginBtn); // 로그인 버튼 추가
        background.add(modeCombo); // 모드 콤보박스 추가
        background.add(diffCombo); // 난이도 콤보박스 추가
    }

    public void gameStart() { // 게임 시작 메서드
        bgm.stop(); // 기존 음악 중지
        if (easterEggs == true) { // 이스터에그 음악 재생
            bgm.play("sounds/easterEggsBgm.wav", true); // 음악 재생
        }
        else { // 기본 음악 재생
            bgm.play("sounds/tetris1.wav", true); // 음악 재생
        }
        int mode = modeCombo.getSelectedIndex(); // 게임 모드 선택
        int diff = diffCombo.getSelectedIndex(); // 난이도 선택
        gamePanel.reset(); // 게임 초기화
        scorePanel.reset(); // 점수 초기화
        scorePanel.setDiff(diff); // 난이도 설정
        scorePanel.setMode(mode); // 모드 설정
        scorePanel.revalidate(); // 점수판 갱신
        scorePanel.repaint(); // 점수판 다시 그리기
        gamePanel.start(mode, diff); // 게임 시작
        gamePanel.getFocus(); // 포커스 설정
    }

    class ImagePanel extends JPanel { // 배경 이미지 패널 클래스
        private Image img; // 이미지 저장

        public ImagePanel(String path) { // 생성자
            img = new ImageIcon(path).getImage(); // 이미지 로드
        }

        @Override
        protected void paintComponent(Graphics g) { // 그림 그리기
            super.paintComponent(g); // 부모 호출
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // 이미지 출력
        }
    }
}
