import javax.swing.JButton; // 버튼 클래스
import javax.swing.JDialog; // 다이얼로그 클래스
import javax.swing.JFrame; // 프레임 클래스
import javax.swing.JLabel; // 라벨 클래스

import java.awt.event.ActionEvent; // 액션 이벤트
import java.awt.event.ActionListener; // 액션 리스너

import java.awt.FlowLayout; // 레이아웃 클래스
import java.awt.Color; // 색상 클래스
import java.awt.Container; // 컨테이너 클래스

public class LoginDialog extends JDialog { // 로그인 다이얼로그 클래스
    private CreateDialog createDialog = new CreateDialog(this); // 회원가입 다이얼로그
    private ContinueDialog continueDialog; // 기존 계정 다이얼로그

    private MyToolBar toolBar; // 툴바 참조
    private JLabel statusLa; // 상태 라벨 참조
    private JButton continueBtn = new JButton("Continue"); // 기존 계정 버튼
    private JButton newBtn = new JButton("Create New Account"); // 새 계정 버튼
    private Container c = getContentPane(); // 컨테이너 가져오기
    private GamePanel gamePanel; // 게임 패널 참조
    private EffectSound es = new EffectSound(); // 효과음 객체

    public LoginDialog(JFrame frame, String title, MyToolBar toolBar, GamePanel gamePanel, JLabel statusLa) { // 생성자
        super(frame, title, true); // 모달 다이얼로그 설정
        this.toolBar = toolBar; // 툴바 설정
        this.gamePanel = gamePanel; // 게임 패널 설정
        this.statusLa = statusLa; // 상태 라벨 설정
        continueDialog = new ContinueDialog(this, toolBar, gamePanel, statusLa); // 기존 계정 다이얼로그 생성
        setLayout(new FlowLayout()); // 레이아웃 설정
        c.setBackground(new Color(0, 1, 49)); // 배경색 설정

        continueBtn.setBackground(new Color(154, 154, 154)); // 기존 계정 버튼 색상
        newBtn.setBackground(new Color(154, 154, 154)); // 새 계정 버튼 색상
        add(continueBtn); // 기존 계정 버튼 추가
        add(newBtn); // 새 계정 버튼 추가

        setSize(200, 110); // 다이얼로그 크기 설정
        setVisible(false); // 초기에는 보이지 않음

        continueBtn.addActionListener(new ActionListener() { // 기존 계정 버튼 클릭
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 버튼 효과음 재생
                setVisible(false); // 로그인 다이얼로그 닫기
                continueDialog.setLocationRelativeTo(null); // 다이얼로그 중앙 배치
                continueDialog.setVisible(true); // 기존 계정 다이얼로그 표시
            }
        });

        newBtn.addActionListener(new ActionListener() { // 새 계정 버튼 클릭
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 버튼 효과음 재생
                setVisible(false); // 로그인 다이얼로그 닫기
                createDialog.setLocationRelativeTo(null); // 다이얼로그 중앙 배치
                createDialog.setVisible(true); // 회원가입 다이얼로그 표시
            }
        });
    }

    public ContinueDialog getContinueDialog() { // 기존 계정 다이얼로그 반환
        return continueDialog; 
    }
}
