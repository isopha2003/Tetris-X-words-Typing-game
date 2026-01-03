import javax.swing.JPanel; // JPanel 임포트
import javax.swing.JLabel; // JLabel 임포트

import java.awt.BorderLayout; // BorderLayout 임포트
import java.awt.Color; // Color 임포트
import java.awt.GridLayout; // GridLayout 임포트
import java.io.BufferedReader; // BufferedReader 임포트
import java.io.File; // File 임포트
import java.io.FileReader; // FileReader 임포트
import java.io.IOException; // IOException 임포트
import java.util.Collections; // Collections 임포트
import java.util.Vector; // Vector 임포트

public class RankingPanel extends JPanel { // RankingPanel 클래스 정의
    private JLabel rankingLa = new JLabel("RANKING"); // 랭킹 라벨 생성

    private File timeRankFile = new File("files/timeAttackRanking.txt"); // 타임어택 랭킹 파일
    private File scoreRankFile = new File("files/scoreRanking.txt"); // 점수 랭킹 파일

    private Vector<UserInfo> timeAttackV = new Vector<UserInfo>(); // 타임어택 벡터
    private Vector<UserInfo> scoreV = new Vector<UserInfo>(); // 점수 벡터

    private SubRankingPanel timeRankingPanel = new SubRankingPanel(timeAttackV, 1); // 타임어택 패널
    private SubRankingPanel scoreRankingPanel = new SubRankingPanel(scoreV, 2); // 점수 패널

    private int mode; // 모드 저장 변수

    public RankingPanel() { // 생성자
        setLayout(new BorderLayout()); // 레이아웃 BorderLayout 설정
        rankingLa.setForeground(new Color(232, 31, 31)); // 라벨 색상 설정
        setBackground(new Color(33, 33, 71)); // 패널 배경색 설정
        rankingLa.setHorizontalAlignment(JLabel.CENTER); // 라벨 중앙 정렬
        add(rankingLa, BorderLayout.NORTH); // 라벨을 상단에 추가
        add(new BothRankPanel(), BorderLayout.CENTER); // 양쪽 랭킹 패널 중앙 배치
    }

    public SubRankingPanel getTimeRankingPanel() { // 타임어택 패널 반환
        return timeRankingPanel; // 패널 반환
    }

    public SubRankingPanel getScoreRankingPanel() { // 점수 패널 반환
        return scoreRankingPanel; // 패널 반환
    }

    class UserInfo implements Comparable<UserInfo> { // 유저 정보 클래스
        private String name; // 이름 저장
        private int score; // 점수 저장
        public UserInfo(String name, int score) { // 생성자
            this.name = name; // 이름 저장
            this.score = score; // 점수 저장
        }
        public String getName() { // 이름 반환
            return name; // 이름 반환
        }
        public int getScore() { // 점수 반환
            return score; // 점수 반환
        }
        @Override
        public int compareTo(UserInfo o) { // 점수 비교
            return Integer.compare(o.score, this.score); // 내림차순 정렬
        }
    }

    public void rangeArray(Vector<UserInfo> v) { // 벡터 정렬
        Collections.sort(v); // 벡터 정렬
    }

    class SubRankingPanel extends JPanel { // 서브 랭킹 패널 클래스
        private Vector<UserInfo> v; // 벡터 저장
        private int mode; // 모드 저장
        public SubRankingPanel(Vector<UserInfo> v, int mode) { // 생성자
            this.v = v; // 벡터 저장
            this.mode = mode; // 모드 저장
            inputV(); // 파일에서 데이터 입력
            setBackground(new Color(33, 33, 71)); // 배경색 설정
            setLayout(new GridLayout(10, 3)); // 10x3 그리드 레이아웃 설정
            for (int i = 0; i < 10; i++) { // 10개 랭킹 반복
                if(v.size() <= i) { // 벡터 크기 체크
                    break; // 반복 종료
                }
                JLabel ranking = new JLabel(); // 랭킹 라벨 생성
                ranking.setText(Integer.toString(i + 1)); // 랭킹 텍스트 설정
                ranking.setForeground(Color.RED); // 색상 설정

                JLabel name = new JLabel(); // 이름 라벨 생성
                name.setText(v.get(i).getName()); // 이름 설정
                name.setForeground(Color.RED); // 색상 설정

                JLabel score = new JLabel(); // 점수 라벨 생성
                score.setText(Integer.toString(v.get(i).getScore())); // 점수 설정
                score.setForeground(Color.RED); // 색상 설정

                add(ranking); // 랭킹 라벨 추가
                add(name); // 이름 라벨 추가
                add(score); // 점수 라벨 추가
            }
            for (int i = v.size(); i < 10; i++) { // 부족한 줄 빈 라벨로 채움
                add(new JLabel("")); // 빈 라벨 추가
                add(new JLabel("")); // 빈 라벨 추가
                add(new JLabel("")); // 빈 라벨 추가
            }
        }

        public void reset() { // 리셋 메서드
            removeAll(); // 기존 컴포넌트 제거
            v.clear(); // 벡터 초기화
            inputV(); // 파일에서 데이터 입력
            setLayout(new GridLayout(10, 3)); // 레이아웃 재설정
            for (int i = 0; i < 10; i++) { // 10개 반복
                if(v.size() <= i) { // 벡터 크기 체크
                    break; // 반복 종료
                }
                JLabel ranking = new JLabel(); // 랭킹 라벨 생성
                ranking.setText(Integer.toString(i + 1)); // 랭킹 텍스트 설정
                ranking.setForeground(Color.RED); // 색상 설정

                JLabel name = new JLabel(); // 이름 라벨 생성
                name.setText(v.get(i).getName()); // 이름 설정
                name.setForeground(Color.RED); // 색상 설정

                JLabel score = new JLabel(); // 점수 라벨 생성
                score.setText(Integer.toString(v.get(i).getScore())); // 점수 설정
                score.setForeground(Color.RED); // 색상 설정

                add(ranking); // 랭킹 라벨 추가
                add(name); // 이름 라벨 추가
                add(score); // 점수 라벨 추가
            }
            for (int i = v.size(); i < 10; i++) { // 부족한 줄 빈 라벨로 채움
                add(new JLabel("")); // 빈 라벨 추가
                add(new JLabel("")); // 빈 라벨 추가
                add(new JLabel("")); // 빈 라벨 추가
            }
            revalidate(); // 레이아웃 갱신
            repaint(); // 화면 갱신
        }

        public void inputV() { // 파일에서 벡터 입력
            File file; // 파일 저장
            Vector<UserInfo> v; // 벡터 저장
            if (mode == 1) { // 타임어택 모드
                file = new File("files/timeAttackRanking.txt"); // 파일 설정
                v = timeAttackV; // 벡터 설정
            } else if(mode == 2) { // 점수 모드
                file = new File("files/scoreRanking.txt"); // 파일 설정
                v = scoreV; // 벡터 설정
            } else { // 잘못된 모드
                return; // 종료
            }
            try { // 예외 처리
                BufferedReader br = new BufferedReader (new FileReader(file)); // 파일 읽기
                String line; // 한 줄 저장
                while((line = br.readLine()) != null) { // 끝까지 반복
                    String nameInfo = line.split(",")[0]; // 이름 추출
                    int scoreInfo = Integer.parseInt(line.split(",")[1]); // 점수 추출
                    v.add(new UserInfo(nameInfo, scoreInfo)); // 벡터에 추가
                }
                rangeArray(v); // 벡터 정렬
            } catch(IOException e) { // 예외 발생 시
            }
        }
    }

    class BothRankPanel extends JPanel { // 양쪽 랭킹 패널 클래스
        public BothRankPanel() { // 생성자
            setLayout(new GridLayout(1, 2, 1, 1)); // 1x2 그리드
            setBackground(new Color(33, 33, 71)); // 배경색 설정
            add(timeRankingPanel); // 타임어택 패널 추가
            add(scoreRankingPanel); // 점수 패널 추가
        }
    }
}
