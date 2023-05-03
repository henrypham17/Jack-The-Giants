package clouds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import helpers.GameInfo;

public class Cloud extends Sprite {

    private World world;  // doi tuong the gioi vat li Box2D de tao và quan ly cac dam may
    private Body body;  // dai dien cho the tich va cac thuoc tinh cua dam may
    private String cloudName;
    private boolean drawLeft;   // xac dinh cach hien thi cua hinh anh dam may

    // Ham khoi tao
    public Cloud(World world, String cloudName){
        // su dung cloudName de tao doi tuong Texture bang cach nap hinh anh tu thu muc Clouds
        super(new Texture("Clouds/" + cloudName + ".png"));
        this.world = world;
        this.cloudName = cloudName;
    }

    // Ham createBody() duoc su dung de tao doi tuong Body va gan nó voi dam may
    void createBody(){
        // Tao doi tuong BodyDef de dinh nghia cac thuoc tinh cua doi tuong Body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;   // o day đoi tuong Body duoc dinh nghia o the tinh

        // bodyDef.position duoc dat vi tri len man hinh, duoc tinh bang cach lay vi tri hien tai cua Sprite (tinh tu goc trai man hinh)
        bodyDef.position.set((getX() - 45) / GameInfo.PPM,
                getY() / GameInfo.PPM);

        body = world.createBody(bodyDef);  // sau do chuyen doi no thanh vi tri Body trong the gioi Box2D

        // tao doi tuong PolygonShape dai dien cho hinh anh cua dam may
        PolygonShape shape = new PolygonShape();
        // Hinh dang nay la 1 HCN, voi kich thuoc duoc tinh bang cach lay kich thuoc cua Sprite / PPM
        shape.setAsBox((getWidth() / 2 - 25) /GameInfo.PPM,
                (getHeight() / 2 - 10) / GameInfo.PPM);

        // doi tuong FixtureDef duoc tao ra de thiet lap cac thuoc tinh cho Fixture cua Body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;  // shape duoc dat la hinh dang PolygonShape vua duoc tao ra truoc do

        // Fixture duoc tao bang cach su dung body.createFixture(fixtureDef)
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(cloudName);  // sau do duoc dat gia tri userData, de co the xac dinh doi tuong la dam may nao trong tro choi

        shape.dispose();  // giai phong tai nguyen cua hinh dang PolygonShape
    }

    // Ham su dung de dat vi tri cua Sprite lên man hinh va tao doi tuong vat ly cho dam may
    public void setSpritePosition(float x, float y){
        setPosition(x, y);
        createBody();
    }

    public String getCloudName(){
        return this.cloudName;
    }

    public boolean getDrawLeft() {
        return drawLeft;
    }

    public void setDrawLeft(boolean drawLeft) {
        this.drawLeft = drawLeft;
    }
}
