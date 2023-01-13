const {ccclass, property} = cc._decorator;

@ccclass
export default class BirdControl extends cc.Component {

    onLoad () {
        cc.director.getPhysicsManager().enabled = true;

        // 打出一条射线
        let results = cc.director.getPhysicsManager().rayCast(this.node.getPosition(),cc.v2(this.node.x,this.node.y + 100),cc.RayCastType.Closest);
        for (let index = 0; index < results.length; index++) {
            let result = results[index];
            // 射线碰到的碰撞器
            // result.collider.tag
            // 射线碰到的点
            // result.point
            // 射线碰到的法线
            // result.normal
            
        }
    }

    start () {
        // this.node.opacity
        // this.node.isValid
        let rbody = this.getComponent(cc.RigidBody);
        // 力
        // rbody.applyForce(cc.v2(1000,0),cc.v2(0,0),true);
        // rbody.applyForceToCenter(cc.v2(5000,0),true);

        // 速度
        rbody.linearVelocity = cc.v2(20,0);
    }

    // 开始碰撞 需要开启刚体的接触接听器
    onBeginContact(contact,self,other){
        // 碰撞点
        let points =  contact.getWorldManifold().points;
        
        // 法线信息
        let normal =  contact.getWorldManifold().normal;

        this.node.color = cc.Color.RED;
        console.log("开始碰撞  " + points[0] , normal);
    }

    // 结束碰撞
    onEndContact(contact,self,other){
        this.node.color = cc.Color.WHITE;
        console.log("结束碰撞");
    }  

    // update (dt) {}
}
