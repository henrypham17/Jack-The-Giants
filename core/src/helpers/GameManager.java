package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class GameManager {  // quản lý dữ liệu trò chơi và điều khiển các hoạt động trong trò chơi. GameManager là 1 class singleton vì nó có một hàm tạo riêng và
                           // một thể hiện tĩnh của chính nó, để đảm bảo rằng chỉ có một đối tượng GameManager được tạo ra trong suốt quá trình chạy của ứng dụng.
    private static GameManager ourInstance = new GameManager(); // biến static kiểu GameManager, chứa instance của lớp GameManager.
    public GameData gameData; // đối tượng GameData chứa dữ liệu game, như high score, điểm, số mạng, số tiền
    private Json json = new Json(); // đối tượng Json để chuyển đổi đối tượng GameData thành chuỗi json và ngược lại
    private FileHandle fileHandle = Gdx.files.local("bin/GameData.json");  // đối tượng FileHandle lưu trữ và truy cập tệp dữ liệu game
    public  boolean gameStartedFromMainMenu, isPaused = true;  // gameStartedFromMainMenu: đánh dấu trạng thái bắt đầu chơi game từ menu chính
                                                               // isPaused: đánh dấu trạng thái tạm dừng trò chơi
    public int LifeScore, coinScore, score; // lưu trữ số mạng, số coin và điểm số của người chơi.
    private Music music; // phát nền nhạc cho game
    private GameManager(){

    }


    // khởi tạo dữ liệu trò chơi ban đầu
    public void initializeGameData(){
        // Nó kiểm tra xem tệp dữ liệu trò chơi có tồn tại hay không và nếu không, sẽ tạo một đối tượng GameData mới với các giá trị mặc định
        // và lưu nó vào tệp. Nếu tệp tồn tại, nó sẽ tải dữ liệu trò chơi từ tệp

        if(!fileHandle.exists()){
            // Nếu nó không tồn tại, phương thức này sẽ tạo một đối tượng GameData mới và đặt một số giá trị mặc định cho trò chơi,
            // chẳng hạn như điểm cao, điểm cao xu và độ khó của trò chơi. Nó cũng tắt nhạc theo mặc định.
            gameData = new GameData();

            gameData.setHighscore(0);
            gameData.setCoinHighscore(0);

            gameData.setEasyDifficulty(false);
            gameData.setMediumDifficulty(true);
            gameData.setHardDifficulty(false);

            gameData.setMusicOn(false);

            saveData();
        } else {
            loadData();  // Nếu tệp đã tồn tại, phương thức này chỉ cần tải dữ liệu trò chơi từ tệp bằng phương thức loadData()
        }
    }

    // lưu trữ dữ liệu trò chơi vào tệp JSON
    public void saveData(){  // phương thức saveData() sẽ kiểm tra xem đối tượng gameData có phải là null hay không.
        // Nếu gameData khác là null, nó sẽ chuyển đổi đối tượng gameData thành một chuỗi JSON bằng phương thức json.prettyPrint().
        // Sau đó, phương thức Base64Coder.encodeString() được sử dụng để mã hóa chuỗi JSON thành chuỗi được mã hóa base-64.
        // Cuối cùng, chuỗi đã mã hóa được ghi vào fileHandle bằng phương thức writeString().
        if(gameData != null){
            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)), false);
        }
    }

    // tải dữ liệu trò chơi từ tệp JSON
    public void loadData(){  // phương thức loadData() đọc dữ liệu JSON từ fileHandle bằng phương thức readString()
        // phương thức Base64Coder.decodeString() được sử dụng để giải mã chuỗi được mã hóa base-64 trở lại chuỗi JSON.
        // Phương thức json.fromJson() được sử dụng để chuyển đổi chuỗi JSON thành đối tượng GameData.
        // Đối tượng gameData này sau đó được gán cho biến gameData cấp độ lớp.
        gameData = json.fromJson(GameData.class, Base64Coder.decodeString(fileHandle.readString()));
    }

    // kiểm tra và cập nhật điểm số cao nhất và số coin cao nhất
    public void checkForNewHighScores(){

        // lấy ra 2 giá trị điểm số cao nhất từ đối tượng gameData đã lưu trữ trước đó
        int oldHighscore = gameData.getHighscore();
        int oldCoinScore = gameData.getCoinHighscore();

        // so sánh các giá trị điểm số mới nhất với điểm số cao nhất hiện tại
        if(oldHighscore < score){
            gameData.setHighscore(score);
        }
        if(oldCoinScore < coinScore){
            gameData.setCoinHighscore(coinScore);
        }

        saveData(); // phương thức saveData() để lưu trữ lại dữ liệu gameData đã được cập nhật với các giá trị mới nhấ
    }

    // phát nhạc nền trong trò chơi
    public void playMusic(){
        if(music == null){  // kiểm tra xem biến music có giá trị là null hay không
            // nó tạo một đối tượng music mới từ tệp âm thanh "Sounds/Background.mp3" và lưu trữ vào biến music
            music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Background.mp3"));
        }

        if(!music.isPlaying()){  // kiểm tra xem đối tượng music đang được phát hay không
            music.play();  // nếu music chưa được phát, nó sẽ gọi phương thức play() để phát nhạc nền.
        }
    }

    // dừng nhạc nền trong trò chơi
    public void stopMusic(){
        if(music.isPlaying()){
            music.stop();
            music.dispose();
        }
    }

    public static GameManager getInstance(){
        return ourInstance;
    }
}
