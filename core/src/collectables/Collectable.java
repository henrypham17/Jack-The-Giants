package collectables;  // được sử dụng để tạo các vật phẩm có thể thu nhập được trong game.

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import helpers.GameInfo;

public class Collectable extends Sprite {

    private World world;
    private Body body;
    private String name;
    private Fixture fixture;

    // Hàm khởi tạo
    public Collectable(World world, String name){
        super(new Texture("Collectables/" + name + ".png"));
        this.world = world;
        this.name = name;
    }

    // tạo vật thể vật lý cho Collectable
    void  createCollectableBody(){
        // đối tượng BodyDef được tạo ra để định nghĩa thông tin về vật thể (body) sẽ được tạo ra.
        // trong đó, type được thiết lập là StaticBody, tức là vật thể tĩnh không thể di chuyển.
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        //  vị trí của vật thể được thiết lập
        bodyDef.position.set((getX() - getWidth() / 2 - 20) / GameInfo.PPM,
                (getY() + getWidth() / 2) / GameInfo.PPM);

        body = world.createBody(bodyDef); // Sau khi đối tượng BodyDef đã được thiết lập đầy đủ, chúng ta sử dụng thư
                                          // viện box2d để tạo ra một vật thể mới (body) trong game.

        // tạo một hình dạng đa giác để đại diện cho vật thể. Hình dạng này được thiết lập là
        // hình hộp (box) với chiều rộng và chiều cao là nửa chiều rộng và chiều cao của vật thể.
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2) / GameInfo.PPM,
                (getHeight() / 2) / GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();  // tạo một đối tượng FixtureDef để định nghĩa các thuộc tính của hình dạng đa giác
        fixtureDef.shape = shape;  // shape được thiết lập là hình dạng đa giác đã tạo ở trên
        fixtureDef.filter.categoryBits = GameInfo.COLLECTABLE;  // categoryBits được thiết lập là GameInfo.COLLECTABLE để xác định loại vật phẩm
        fixtureDef.isSensor = true;  // isSensor được thiết lập là true để chỉ định rằng đây là một cảm biến (sensor) và không có va chạm

        fixture = body.createFixture(fixtureDef);  //  fixture được tạo ra bằng cách gắn kết kết cấu này với thân của đối tượng
        fixture.setUserData(name);   // setUserData() được gọi để gắn tên của đối tượng vào fixture

        shape.dispose();
    }

    public void setCollectablePosition(float x, float y){  // thiết lập vị trí của Collectable
        setPosition(x, y);
        createCollectableBody();
    }

    public void updateCollectacble(){  // cập nhật vị trí của Collectable
        setPosition(body.getPosition().x * GameInfo.PPM, (body.getPosition().y - 0.2f) * GameInfo.PPM);
    }

    // thay đổi phân loại của Fixture của Collectable để xác định xem nó có thể va chạm với những đối tượng nào trong thế giới Box2D
    public void changeFilter(){
        Filter filter = new Filter();  // tạo ra một đối tượng Filter (bộ lọc) mới
        filter.categoryBits = GameInfo.DESTROYED;  // đối tượng filter này được thiết lập với categoryBits bằng GameInfo.DESTROYED
                  // có nghĩa là khi đối tượng này va chạm với các đối tượng khác, nó sẽ được xử lý như là một đối tượng đã bị phá hủy

        fixture.setFilterData(filter);  // phương thức setFilterData() được gọi trên fixture của đối tượng để đặt lại bộ lọc này vào Fixture.
    }

    public  Fixture getFixture(){
        return fixture;
    }
}
