import javax.swing.JButton; // 버튼 클래스
import javax.swing.JDialog; // 다이얼로그 창
import javax.swing.JLabel; // 라벨
import javax.swing.JPanel; // 패널
import javax.swing.JPasswordField; // 비밀번호 입력 필드
import javax.swing.JTextField; // 텍스트 입력 필드
import javax.swing.event.DocumentEvent; // 문서 이벤트
import javax.swing.event.DocumentListener; // 문서 리스너

import java.awt.BorderLayout; // BorderLayout
import java.awt.Color; // 색상
import java.awt.Container; // 컨테이너
import java.awt.FlowLayout; // FlowLayout
import java.awt.GridLayout; // GridLayout
import java.awt.event.ActionEvent; // 액션 이벤트
import java.awt.event.ActionListener; // 액션 리스너
import java.io.BufferedReader; // 파일 읽기 버퍼
import java.io.File; // 파일
import java.io.FileReader; // 파일 리더
import java.io.FileWriter; // 파일 작성기
import java.io.IOException; // 입출력 예외

public class CreateDialog extends JDialog { // 회원가입 다이얼로그 클래스

    private JLabel info = new JLabel("Please enter your ID and password"); // 안내 메시지
    private JLabel idLa = new JLabel("ID "); // ID 라벨
    private JLabel passwordLa = new JLabel("Password "); // 비밀번호 라벨
    private JLabel passwordConfirmLa = new JLabel("Confirm Password"); // 비밀번호 확인 라벨

    private JTextField id = new JTextField(10); // ID 입력창
    private JPasswordField password = new JPasswordField(10); // 비밀번호 입력창
    private JPasswordField confirmPassword = new JPasswordField(10); // 비밀번호 확인 입력창

    private Container c = getContentPane(); // 다이얼로그 컨테이너

    String inputId; // 입력된 ID
    char[] inputPassword; // 입력된 비밀번호(char 배열)
    String stringPassword; // 비밀번호 문자열
    char[] inputConfirmPassword; // 확인 비밀번호(char 배열)
    String stringConfirmPassword; // 확인 비밀번호 문자열

    private JButton createBtn = new JButton("Create"); // 계정 생성 버튼

    private File userInfoFile = new File("files/userInfo.txt"); // 사용자 정보 파일

    private EffectSound es = new EffectSound(); // 효과음 객체

    public CreateDialog(JDialog dialog) {
        super(dialog, true); // 부모 다이얼로그, 모달 설정
        setLayout(new FlowLayout()); // 레이아웃 설정
        c.setBackground(new Color(0, 1, 49)); // 배경색 설정

        info.setForeground(Color.RED); // 안내 문구 색상
        add(info, BorderLayout.NORTH); // 안내 문구 추가

        createBtn.setEnabled(false); // 버튼 비활성화
        add(new CreatePanel()); // 입력 패널 추가

        createBtn.setEnabled(false); // 버튼 비활성화 유지
        createBtn.setBackground(new Color(154, 154, 154)); // 버튼 색상

        id.getDocument().addDocumentListener(docListener); // ID 입력 감지
        password.getDocument().addDocumentListener(docListener); // 비밀번호 입력 감지
        confirmPassword.getDocument().addDocumentListener(docListener); // 확인 비밀번호 입력 감지

        confirmPassword.addActionListener(new ActionListener() { // 엔터 입력 시
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 효과음 재생
                try {
                    FileWriter userInfoOut = new FileWriter("files/userInfo.txt", true); // 파일 열기
                    String line = inputId + "," + stringPassword; // ID,비밀번호 생성
                    userInfoOut.write(line + "\n"); // 파일에 저장
                    userInfoOut.close(); // 파일 닫기
                } catch (IOException e1) {
                    System.out.println("입출력 오류"); // 오류 출력
                }
                setVisible(false); // 다이얼로그 닫기
                id.setText(""); // ID 초기화
                password.setText(""); // 비밀번호 초기화
            }
        });

        createBtn.addActionListener(new ActionListener() { // Create 버튼 클릭 시
            @Override
            public void actionPerformed(ActionEvent e) {
                es.play("sounds/buttonBgm.wav", +6.02f); // 효과음 재생
                try {
                    FileWriter userInfoOut = new FileWriter("files/userInfo.txt", true); // 파일 열기
                    String line = inputId + "," + stringPassword; // ID,비밀번호 생성
                    userInfoOut.write(line + "\n"); // 파일에 저장
                    userInfoOut.close(); // 파일 닫기
                } catch (IOException e1) {
                    System.out.println("입출력 오류"); // 오류 출력
                }
                setVisible(false); // 다이얼로그 닫기
                id.setText(""); // ID 초기화
                password.setText(""); // 비밀번호 초기화
            }
        });

        add(createBtn); // 버튼 추가
        setSize(290, 200); // 다이얼로그 크기 설정
    }

    private DocumentListener docListener = new DocumentListener() { // 입력 변경 감지
        @Override
        public void insertUpdate(DocumentEvent e) { check(); } // 입력 추가
        @Override
        public void removeUpdate(DocumentEvent e) { check(); } // 입력 삭제
        @Override
        public void changedUpdate(DocumentEvent e) { check(); } // 입력 변경
    };

    private void check() { // 입력 검증 메서드
        boolean idConfirm = true; // ID 사용 가능 여부
        boolean passwordConfirm = true; // 비밀번호 확인 여부

        inputId = id.getText(); // ID 저장
        try {
            BufferedReader br = new BufferedReader(new FileReader(userInfoFile)); // 파일 읽기
            String line;
            while ((line = br.readLine()) != null) { // 파일 끝까지 반복
                if (inputId.equals(line.split(",")[0])) { // ID 중복 검사
                    info.setText("This ID already exists."); // 중복 메시지
                    idConfirm = false; // ID 불가
                    break;
                } else {
                    info.setText("Please enter your password."); // 다음 단계 안내
                }
            }
        } catch (IOException e) {
        }

        inputPassword = password.getPassword(); // 비밀번호 입력
        stringPassword = new String(inputPassword); // 문자열 변환
        inputConfirmPassword = confirmPassword.getPassword(); // 확인 비밀번호
        stringConfirmPassword = new String(inputConfirmPassword); // 문자열 변환

        if (stringPassword.isEmpty() || stringConfirmPassword.isEmpty()) {
            passwordConfirm = false; // 빈 값이면 실패
        }

        if (!stringPassword.equals(stringConfirmPassword)) { // 비밀번호 비교
            info.setText("Passwords do not match."); // 오류 메시지
            passwordConfirm = false; // 실패 처리
        }

        if (!inputId.isEmpty() && idConfirm && passwordConfirm) { // 모든 조건 통과
            info.setText("Welcome!"); // 성공 메시지
            createBtn.setEnabled(true); // 버튼 활성화
        } else {
            createBtn.setEnabled(false); // 버튼 비활성화
        }
    }

    class CreatePanel extends JPanel { // 입력 패널 클래스
        public CreatePanel() {
            setLayout(new GridLayout(3, 2, 10, 10)); // 3x2 그리드
            setBackground(new Color(0, 1, 49)); // 배경색
            idLa.setForeground(new Color(154, 154, 154)); // ID 라벨 색
            passwordLa.setForeground(new Color(154, 154, 154)); // 비밀번호 라벨 색
            passwordConfirmLa.setForeground(new Color(154, 154, 154)); // 확인 라벨 색
            add(idLa); // ID 라벨 추가
            add(id); // ID 필드 추가
            add(passwordLa); // 비밀번호 라벨 추가
            add(password); // 비밀번호 필드 추가
            add(passwordConfirmLa); // 확인 라벨 추가
            add(confirmPassword); // 확인 필드 추가
        }
    }
}
