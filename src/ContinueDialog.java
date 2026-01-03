import javax.swing.JButton; // 버튼 클래스
import javax.swing.JDialog; // 다이얼로그 창
import javax.swing.JLabel; // 텍스트 라벨
import javax.swing.JPanel; // 패널
import javax.swing.JPasswordField; // 비밀번호 입력 필드
import javax.swing.JTextField; // 텍스트 입력 필드
import javax.swing.event.DocumentListener; // 문서 변경 리스너
import javax.swing.event.DocumentEvent; // 문서 이벤트

import java.awt.Container; // 컨테이너
import java.awt.FlowLayout; // FlowLayout
import java.awt.GridLayout; // GridLayout
import java.awt.event.ActionEvent; // 액션 이벤트
import java.awt.event.ActionListener; // 액션 리스너
import java.io.BufferedReader; // 파일 읽기 버퍼
import java.io.File; // 파일 클래스
import java.io.FileReader; // 파일 리더
import java.io.IOException; // 입출력 예외
import java.awt.Color; // 색상 클래스

public class ContinueDialog extends JDialog { // 로그인 다이얼로그 클래스

    private GamePanel gamePanel; // 게임 패널 객체

    private JLabel info = new JLabel("Please enter your ID and password"); // 안내 문구
    private JLabel idLa = new JLabel("ID "); // ID 라벨
    private JLabel passwordLa = new JLabel("Password "); // 비밀번호 라벨
    private JLabel statusLa; // 상태 라벨

    private MyToolBar toolBar; // 툴바 객체

    private JTextField id = new JTextField(10); // ID 입력창
    private JPasswordField password = new JPasswordField(10); // 비밀번호 입력창

    private Container c = getContentPane(); // 다이얼로그 컨테이너

    private File userInfoFile = new File("files/userInfo.txt"); // 사용자 정보 파일

    private JButton loginBtn = new JButton("Login"); // 로그인 버튼
    private JButton logoutBtn = new JButton("Logout"); // 로그아웃 버튼

    private EffectSound es = new EffectSound(); // 효과음 객체

    public ContinueDialog(JDialog dialog, MyToolBar toolBar, GamePanel gamePanel, JLabel statusLa) {
        super(dialog, true); // 부모 다이얼로그 설정 (모달)
        this.statusLa = statusLa; // 상태 라벨 설정
        this.toolBar = toolBar; // 툴바 설정
        this.gamePanel = gamePanel; // 게임 패널 설정

        setLayout(new FlowLayout()); // 레이아웃 설정
        c.setBackground(new Color(0, 1, 49)); // 배경색 설정

        id.getDocument().addDocumentListener(docListener); // ID 입력 감지
        password.getDocument().addDocumentListener(docListener); // 비밀번호 입력 감지

        password.addActionListener(new ActionListener() { // 엔터 입력 시
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 버튼 효과음 재생
                statusLa.setText(id.getText()); // 상태에 ID 표시
                setVisible(false); // 다이얼로그 닫기
                toolBar.removeLoginBtn(); // 로그인 버튼 제거
                toolBar.addLogoutBtn(logoutBtn); // 로그아웃 버튼 추가
                gamePanel.setUser(id.getText()); // 사용자 설정
                id.setText(""); // ID 초기화
                password.setText(""); // 비밀번호 초기화
            }
        });

        info.setForeground(Color.RED); // 안내 문구 색상
        add(info); // 안내 문구 추가
        add(new ContinuePanel()); // 입력 패널 추가

        loginBtn.addActionListener(new ActionListener() { // 로그인 버튼 클릭 시
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 효과음
                statusLa.setText(id.getText()); // 상태 변경
                setVisible(false); // 창 닫기
                toolBar.removeLoginBtn(); // 로그인 버튼 제거
                toolBar.addLogoutBtn(logoutBtn); // 로그아웃 버튼 추가
                gamePanel.setUser(id.getText()); // 사용자 설정
                id.setText(""); // ID 초기화
                password.setText(""); // 비밀번호 초기화
            }
        });

        loginBtn.setBackground(new Color(154, 154, 154)); // 버튼 색상
        loginBtn.setEnabled(false); // 기본 비활성화
        add(loginBtn); // 버튼 추가

        logoutBtn.addActionListener(new ActionListener() { // 로그아웃 버튼 클릭
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 효과음
                statusLa.setText("GUEST"); // 상태를 GUEST로 변경
                gamePanel.setUser(null); // 사용자 제거
                toolBar.removeLogoutBtn(logoutBtn); // 로그아웃 버튼 제거
                toolBar.addLoginBtn(); // 로그인 버튼 추가
            }
        });

        setSize(290, 170); // 다이얼로그 크기 설정
    }

    private DocumentListener docListener = new DocumentListener() { // 입력 변화 감지
        @Override
        public void insertUpdate(DocumentEvent e) { check(); } // 입력 추가
        @Override
        public void removeUpdate(DocumentEvent e) { check(); } // 입력 삭제
        @Override
        public void changedUpdate(DocumentEvent e) { check(); } // 입력 변경
    };

    public void check() { // ID/비밀번호 확인 메서드
        boolean idConfirm = false; // ID 확인 여부
        boolean passwordConfirm = false; // 비밀번호 확인 여부
        String line; // 파일 한 줄 저장

        try {
            BufferedReader br = new BufferedReader(new FileReader(userInfoFile)); // 파일 읽기
            while ((line = br.readLine()) != null) { // 파일 끝까지 반복
                if (id.getText().equals(line.split(",")[0])) { // ID 비교
                    idConfirm = true; // ID 일치
                    break; // 반복 종료
                }
                if (idConfirm == false) {
                    info.setText("No matching ID found."); // ID 없음 메시지
                }
            }

            if (idConfirm == true) { // ID가 맞으면
                char[] inputPassword = password.getPassword(); // 입력 비밀번호
                String stringPassword = new String(inputPassword); // 문자열 변환
                if (stringPassword.equals(line.split(",")[1])) { // 비밀번호 비교
                    passwordConfirm = true; // 비밀번호 일치
                }
                info.setText("Passwords do not match."); // 비밀번호 오류
            }

            if (idConfirm && passwordConfirm) { // 모두 맞으면
                info.setText("Welcome!"); // 환영 메시지
                loginBtn.setEnabled(true); // 로그인 버튼 활성화
            }
        } catch (IOException e) {
            // 예외 처리 (비어 있음)
        }
    }

    class ContinuePanel extends JPanel { // 입력 패널 클래스
        public ContinuePanel() {
            setLayout(new GridLayout(2, 2, 10, 10)); // 2x2 그리드
            setBackground(new Color(0, 1, 49)); // 배경색
            idLa.setForeground(new Color(154, 154, 154)); // ID 라벨 색
            passwordLa.setForeground(new Color(154, 154, 154)); // 비밀번호 라벨 색
            add(idLa); // ID 라벨 추가
            add(id); // ID 필드 추가
            add(passwordLa); // 비밀번호 라벨 추가
            add(password); // 비밀번호 필드 추가
        }
    }

    public String getId() { // ID 반환 메서드
        if (id.getText() != null) { // ID가 있으면
            return id.getText(); // 반환
        }
        return null; // 없으면 null
    }
}
