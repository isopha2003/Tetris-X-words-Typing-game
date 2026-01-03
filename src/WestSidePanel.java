import javax.swing.JPanel; // JPanel 임포트

import java.awt.Color; // Color 임포트
import java.awt.Dimension; // Dimension 임포트
import java.awt.BorderLayout; // BorderLayout 임포트

public class WestSidePanel extends JPanel { // WestSidePanel 클래스 정의
    private int mode; // 게임 모드 저장
    private JPanel panel = new JPanel(); // 왼쪽 배경 패널
    private RankingPanel rankPanel = new RankingPanel(); // 랭킹 패널
    private NextWordPanel nextPanel = new NextWordPanel(); // 다음 단어 패널

    public WestSidePanel() { // 생성자
        panel.setBackground(new Color(154, 154, 154)); // 패널 배경색 설정
        setLayout(new BorderLayout()); // BorderLayout 사용
        setBackground(new Color(154, 154, 154)); // 전체 배경색 설정
        setPreferredSize(new Dimension(300, 200)); // 패널 크기 설정
        
        add(panel, BorderLayout.WEST); // 왼쪽 배경 패널 추가
        add(nextPanel, BorderLayout.NORTH); // 상단 다음 단어 패널 추가
        add(rankPanel, BorderLayout.CENTER); // 중앙 랭킹 패널 추가
    }

    public NextWordPanel getNextWordPanel() { // 다음 단어 패널 반환
        return nextPanel; // nextPanel 반환
    }

    public RankingPanel getRankingPanel() { // 랭킹 패널 반환
        return rankPanel; // rankPanel 반환
    }
}
