// Learn cc.Class:
//  - https://docs.cocos.com/creator/manual/en/scripting/class.html
// Learn Attribute:
//  - https://docs.cocos.com/creator/manual/en/scripting/reference/attributes.html
// Learn life-cycle callbacks:
//  - https://docs.cocos.com/creator/manual/en/scripting/life-cycle-callbacks.html

cc.Class({
    extends: cc.Component,

    properties: {
        G:-160, //重力加速度
        speed:200, // 初速度大小
        degree:45,//抛射角度
        play_onload:false,
    },

    // LIFE-CYCLE CALLBACKS:

    // onLoad () {},

    start () {
        this.is_moving = false;//标记这个物体是否移动
        if(this.play_onload){
            this.throw_action();
        }
    },

    throw_action(){
        var r = this.degree*Math.PI/180;
        this.vx = this.speed*Math.cos(r);
        this.vy = this.speed*Math.sin(r);
        this.is_moving = true;
    },

    update (dt) {
        if(this.is_moving === false){
            return ;
        }
        var sy = this.vy * dt + 0.5 + this.G*dt*dt;
        this.vy = this.vy + this.G *dt;
        this.node.y += sy;
        var w_pos = this.node.convertToWorldSpaceAR(cc.v2(0, 0));
        if (this.out_of_screen(w_pos)) {
            // this.node.removeFromParent(); 
            // console.log("removed");   
            this.is_moving = false;    
            this.re_show();
        }        
    },

    re_show(){
        if(this.is_moving){
            return;
        }
        var x = Math.random() * cc.winSize.width - cc.winSize.width/2;
        var y = -cc.winSize.height * 0.5 +50;
        this.node.setPosition(x,y);
        this.speed = Math.random() * 300 +200;
        this.degree = Math.random()*45 +45;
        this.throw_action()
    },
    out_of_screen(w_pos){
        var money_width = this.node.width/2;
        var money_height = this.node.height/2;
        return w_pos.x < -money_width || w_pos.x > cc.winSize.width + money_width || w_pos.y < -money_height || w_pos.y > cc.winSize.height + money_height;
    },
});
