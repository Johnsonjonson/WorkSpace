// Learn cc.Class:
//  - https://docs.cocos.com/creator/manual/en/scripting/class.html
// Learn Attribute:
//  - https://docs.cocos.com/creator/manual/en/scripting/reference/attributes.html
// Learn life-cycle callbacks:
//  - https://docs.cocos.com/creator/manual/en/scripting/life-cycle-callbacks.html

cc.Class({
    extends: cc.Component,

    properties: {
        is_debug :false,
        speed:100,
        pos:cc.v2(0,0),
        apple : { type : cc.Node, default : null, },
    },

    // LIFE-CYCLE CALLBACKS:

    // onLoad () {},

    start () {
        console.log("start",this,this.node);
    },

    update (dt) {
        // this.apple.x += this.speed; 
        this.apple.x += this.speed * dt;
    },
});
