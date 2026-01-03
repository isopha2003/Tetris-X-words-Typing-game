import javax.swing.JPanel; // 패널 클래스

import java.awt.BorderLayout; // BorderLayout
import java.awt.Color; // 색상 클래스

public class EastSidePanel extends JPanel { // 동쪽 영역 패널 클래스

    private JPanel panel = new JPanel(); // 보조 패널

    public EastSidePanel(ScorePanel scorePanel) { // 생성자
        setLayout(new BorderLayout()); // BorderLayout 설정
        setBackground(new Color(154, 154, 154)); // 배경색 설정

        panel.setBackground(new Color(154, 154, 154)); // 보조 패널 배경색
        add(scorePanel, BorderLayout.CENTER); // 점수 패널을 중앙에 추가

        add(panel, BorderLayout.EAST); // 빈 패널을 동쪽에 추가
    }
}
