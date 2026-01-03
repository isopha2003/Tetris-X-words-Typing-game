import java.io.BufferedReader; // BufferedReader 임포트
import java.io.File; // File 임포트
import java.io.FileReader; // FileReader 임포트
import java.io.IOException; // IOException 임포트
import java.util.Vector; // Vector 임포트

public class SaveWords { // SaveWords 클래스 정의
    private Vector<String> v = new Vector<String>(); // 단어 저장 벡터

    public void saveWords() { // 단어 파일 읽기 메서드
        File file = new File("files/words.txt"); // 단어 파일 객체 생성

        try { // 예외 처리 시작
            BufferedReader br = new BufferedReader(new FileReader(file)); // 파일 읽기
            String line; // 한 줄 저장 변수

            while((line = br.readLine()) != null) { // 파일 끝까지 반복
                v.add(line); // 벡터에 단어 추가
            }

            br.close(); // BufferedReader 닫기
        }
        catch(IOException e) { // IOException 발생 시
            System.out.println("파일 읽기 오류"); // 오류 메시지 출력
        }
    }

    public String get() { // 단어 랜덤 반환 메서드
        int index = (int)(Math.random() * v.size()); // 랜덤 인덱스 생성
        return v.get(index); // 벡터에서 단어 반환
    }
}
