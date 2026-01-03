import javax.swing.JPanel; // JPanel 임포트

import java.awt.Color; // Color 임포트
import java.awt.Dimension; // Dimension 임포트

public class SouthSidePanel extends JPanel { // SouthSidePanel 클래스 정의
    public SouthSidePanel() { // 생성자
        setBackground(new Color(154, 154, 154)); // 배경색 설정
        setPreferredSize(new Dimension(100, 30)); // 패널 크기 설정
    }
}
