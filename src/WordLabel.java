import javax.swing.JLabel; // JLabel 임포트

import java.awt.Color; // Color 임포트
import java.awt.Dimension; // Dimension 임포트
import java.awt.Font; // Font 임포트

public class WordLabel extends JLabel { // WordLabel 클래스 정의
    private Color colors[] = {new Color(52, 184, 178), new Color(73, 129, 197), // 색상 배열
                              new Color(237, 166, 8), new Color(255, 206, 47), 
                              new Color(25, 220, 32), new Color(156, 14, 233),
                              new Color(231, 12, 13)};
    private int colorIndex; // 색상 인덱스
    private SaveWords sw = new SaveWords(); // SaveWords 객체 생성
    private String word; // 단어 저장
    private int x; // 현재 x 위치
    private int width; // 패널 너비
    private Color color; // 색상 저장

    public WordLabel() { // 생성자
        sw.saveWords(); // 단어 목록 읽기
    }

    public void setLabel() { // 라벨 설정
        word = sw.get(); // 랜덤 단어 가져오기
        setText(word); // 텍스트 설정
        setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 폰트 설정
        colorIndex = (int)(Math.random() * colors.length); // 랜덤 색상 선택
        color = colors[colorIndex]; // 색상 설정
        setOpaque(true); // 배경 불투명
        setBackground(color); // 배경 색상 설정
        setForeground(Color.BLACK); // 글자 색상 설정
        setOpaque(true); // 배경 불투명 설정 (중복)
        Dimension d = getPreferredSize(); // 크기 계산
        setSize(d.width + 10, d.height + 20); // 크기 적용

        x = (int)(Math.random() * Math.max(1, width - (d.width + 10))); // 랜덤 x 위치
        setLocation(x, 0); // 위치 설정
    }

    public void setWidth(int width) { // 너비 설정
        this.width = width; // 너비 저장
    }

    public void setWord(String word) { // 단어 설정
        this.word = word; // 단어 저장
        setText(word); // 텍스트 설정

        Dimension d = getPreferredSize(); // 크기 계산
        setSize(d.width + 10, d.height + 20); // 크기 적용
    }

    public void setColor(int colorIndex) { // 색상 설정
        this.color = colors[colorIndex]; // 색상 선택
        setBackground(color); // 배경색 설정
    }

    public int getColorIndex() { // 색상 인덱스 반환
        return colorIndex; // 반환
    }

    public String getWord() { // 단어 반환
        return word; // 반환
    }
}
