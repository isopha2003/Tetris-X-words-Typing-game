import javax.swing.JDialog; // 다이얼로그 클래스

import java.awt.Color; // 색상 클래스
import java.awt.FlowLayout; // FlowLayout
import java.awt.event.ActionEvent; // 액션 이벤트
import java.awt.event.ActionListener; // 액션 리스너
import java.awt.event.WindowAdapter; // 윈도우 이벤트 어댑터
import java.awt.event.WindowEvent; // 윈도우 이벤트
import java.awt.Container; // 컨테이너 클래스

import javax.swing.JButton; // 버튼 클래스

public class PauseDialog extends JDialog { // 일시정지 다이얼로그 클래스
    private JButton continueBtn = new JButton("Continue Game"); // 계속하기 버튼
    private JButton quiteBtn = new JButton("Quit"); // 종료 버튼

    public PauseDialog(GamePanel gamePanel) { // 생성자
        Container c = getContentPane(); // 컨테이너 가져오기
        setLayout(new FlowLayout()); // FlowLayout 설정
        c.setBackground(new Color(0, 1, 49)); // 배경색 설정
        continueBtn.setBackground(new Color(154, 154, 154)); // 계속하기 버튼 색
        quiteBtn.setBackground(new Color(154, 154, 154)); // 종료 버튼 색

        continueBtn.addActionListener(new ActionListener() { // 계속하기 버튼 클릭 이벤트
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // 다이얼로그 닫기
                gamePanel.resumeGame(); // 게임 재개
            }
        });

        quiteBtn.addActionListener(new ActionListener() { // 종료 버튼 클릭 이벤트
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.gameOver(); // 게임 오버 처리
                gamePanel.interruptThread(); // 스레드 중단
                setVisible(false); // 다이얼로그 닫기
            }
        });

        addWindowListener(new WindowAdapter() { // 창 닫기 이벤트 처리
            @Override
            public void windowClosing(WindowEvent e) {
                gamePanel.gameOver(); // 게임 오버 처리
                gamePanel.interruptThread(); // 스레드 중단
            }
        });

        add(continueBtn); // 계속하기 버튼 추가
        add(quiteBtn); // 종료 버튼 추가

        setSize(140, 110); // 다이얼로그 크기 설정
        setVisible(false); // 기본 숨김
    }
}
