package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jackthegiant.GameMain;
import helpers.GameInfo;
import huds.OptionsButtons;

public class Options implements Screen {

    private GameMain game;
    private Viewport gameViewport;
    private OrthographicCamera mainCamera;
    private Texture bg;
    private OptionsButtons btns;

    public Options(GameMain game){
        this.game = game;

        mainCamera = new OrthographicCamera(); // Khởi tạo đối tượng OrthographicCamera với tên là mainCamera, đây là đối tượng camera để hiển thị màn hình trò chơi
        mainCamera.setToOrtho(false, GameInfo.Width, GameInfo.Height);  // thiết lập kiểu chiếu cho camera là Orthographic
        // và đặt kích thước màn hình là GameInfo.Width và GameInfo.Height
        // Thiết lập vị trí ban đầu cho camera bằng cách đặt trung tâm của camera là điểm chính giữa màn hình
        mainCamera.position.set(GameInfo.Width / 2f, GameInfo.Height / 2f, 0);

        // đối tượng StretchViewport với tên là gameViewport và kích thước là GameInfo.Width và GameInfo.Height
        // để xác định kích thước của màn hình và tỷ lệ của nội dung trò chơi được hiển thị trên màn hình
        gameViewport = new StretchViewport(GameInfo.Width, GameInfo.Height, mainCamera);

        bg = new Texture("Backgrounds/Options BG.png");

        // Khởi tạo một đối tượng OptionsButtons với tên là btns bằng cách truyền đối tượng game vào. Đối tượng này chứa các nút (button)
        // để người chơi có thể tương tác với màn hình lựa chọn cấp độ.
        btns = new OptionsButtons(game);
    }

    @Override
    public void show() {

    }

    // được gọi mỗi khung hình để vẽ các đối tượng lên màn hình Highscore.
    @Override
    public void render(float delta) {

        // Sử dụng phương thức glClearColor và glClear từ OpenGL để xóa màn hình bằng màu đỏ
        // Việc này giúp tránh hiển thị những nội dung cũ trên màn hình và chuẩn bị cho việc vẽ các đối tượng mới
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin(); // Gọi phương thức begin của SpriteBatch từ đối tượng game để bắt đầu vẽ các đối tượng lên màn hình

        game.getBatch().draw(bg, 0, 0); // Sử dụng phương thức draw của SpriteBatch từ đối tượng game để vẽ hình ảnh nền (bg) lên màn hình tại vị trí (0, 0)

        game.getBatch().end(); // kết thúc vẽ các đối tượng lên màn hình.

        // Sử dụng phương thức setProjectionMatrix của SpriteBatch từ đối tượng game để đặt ma trận chiếu cho việc vẽ các nút trên màn hình điểm số cao
        // Ma trận chiếu được lấy từ đối tượng camera của đối tượng btns bằng phương thức getCamera và kết hợp với ma trận chiếu của camera chính
        // của trò chơi bằng phương thức combined.
        game.getBatch().setProjectionMatrix(btns.getStage().getCamera().combined);
        btns.getStage().draw(); // để vẽ các nút lên màn hình
    }

    // được gọi khi kích thước của màn hình thay đổi.
    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    // xoá khỏi bộ nhớ và nó giải phóng các tài nguyên không cần thiết chẳng hạn như texture và stage.
    @Override
    public void dispose() {
        btns.getStage().dispose();
    }
}
