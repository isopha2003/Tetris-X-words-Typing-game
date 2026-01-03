import javax.swing.JPanel; // 패널 클래스
import javax.swing.JLabel; // 라벨 클래스

import java.awt.Color; // 색상 클래스
import java.awt.Font; // 폰트 클래스
import java.awt.Dimension; // 크기 클래스
import java.awt.BorderLayout; // BorderLayout

public class NextWordPanel extends JPanel { // 다음 단어 표시 패널
    private Color colors[] = {new Color(52, 184, 178), new Color(73, 129, 197),
              new Color(237, 166, 8), new Color(255, 206, 47),
              new Color(25, 220, 32), new Color(156, 14, 233),
              new Color(231, 12, 13)}; // 색상 배열
    private JPanel panel = new JPanel(); // 좌측 보조 패널
    private JPanel panel2 = new JPanel(); // 하단 보조 패널
    private JLabel nextLabel = new JLabel("NEXT"); // NEXT 라벨
    private JLabel nextFallingLabel = new JLabel(); // 다음 글자 라벨
    private MyPanel myPanel = new MyPanel(); // 중앙 패널
    
    public NextWordPanel() { // 생성자
        setLayout(new BorderLayout()); // BorderLayout 설정
        panel.setBackground(new Color(154, 154, 154)); // 좌측 패널 색
        panel2.setBackground(Color.BLACK); // 하단 패널 색
        add(myPanel, BorderLayout.CENTER); // 중앙 패널 추가
        add(panel, BorderLayout.WEST); // 좌측 패널 추가
        setBackground(new Color(33, 33, 71)); // 전체 배경색
    }
    
    class MyPanel extends JPanel { // 중앙 패널 클래스
        public MyPanel() { 
            setLayout(new BorderLayout()); // BorderLayout 설정
            nextLabel.setForeground(new Color(255, 0, 0)); // NEXT 글자색
            nextLabel.setHorizontalAlignment(JLabel.CENTER); // 중앙 정렬
            nextFallingLabel.setPreferredSize(new Dimension(120, 50)); // 크기 설정
            add(nextLabel, BorderLayout.NORTH); // NEXT 라벨 상단 배치
            add(nextFallingLabel, BorderLayout.CENTER); // 다음 글자 중앙 배치
            setBackground(Color.BLACK); // 배경색
        }
    }
    
    public void setPanel(WordLabel newLabel) { // 다음 단어 표시 메서드
        myPanel.removeAll(); // 기존 중앙 컴포넌트 제거
        String nextWord = newLabel.getWord(); // 다음 단어 가져오기
        int nextColorIndex = newLabel.getColorIndex(); // 다음 글자 색 인덱스 가져오기
        nextFallingLabel.setText(nextWord); // 다음 단어 텍스트 설정
        nextFallingLabel.setBackground(colors[nextColorIndex]); // 배경색 설정
        nextFallingLabel.setOpaque(true); // 배경색 적용
        nextFallingLabel.setHorizontalAlignment(JLabel.CENTER); // 중앙 정렬
        nextFallingLabel.setVerticalAlignment(JLabel.CENTER); // 수직 중앙 정렬
        nextFallingLabel.setPreferredSize(new Dimension(120, 50)); // 크기 재설정
        nextFallingLabel.setVisible(true); // 라벨 보이기
        nextFallingLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 글꼴 설정

        myPanel.add(nextLabel, BorderLayout.NORTH); // NEXT 라벨 상단 추가
        myPanel.add(nextFallingLabel, BorderLayout.CENTER); // 다음 글자 중앙 추가
        myPanel.add(panel2, BorderLayout.SOUTH); // 하단 패널 추가

        myPanel.revalidate(); // 레이아웃 갱신
        myPanel.repaint(); // 화면 갱신
    }
}
