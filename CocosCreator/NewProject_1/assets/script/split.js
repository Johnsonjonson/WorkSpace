// Learn cc.Class:
//  - https://docs.cocos.com/creator/manual/en/scripting/class.html
// Learn Attribute:
//  - https://docs.cocos.com/creator/manual/en/scripting/reference/attributes.html
// Learn life-cycle callbacks:
//  - https://docs.cocos.com/creator/manual/en/scripting/life-cycle-callbacks.html

const LoginUtils = require("login/LoginUtils");
cc.Class({
    extends: cc.Component,

    properties: {
        line : { type : cc.Node, default : null, },
        // foo: {
        //     // ATTRIBUTES:
        //     default: null,        // The default value will be used only when the component attaching
        //                           // to a node for the first time
        //     type: cc.SpriteFrame, // optional, default is typeof default
        //     serializable: true,   // optional, default is true
        // },
        // bar: {
        //     get () {
        //         return this._bar;
        //     },
        //     set (value) {
        //         this._bar = value;
        //     }
        // },
    },

    // LIFE-CYCLE CALLBACKS:

    onLoad () {
        var manager = cc.director.getCollisionManager();
        manager.enabled = trur;
        manager.enabledDebugDraw = true;
        manager.enabledDrawBoundingBox = true;
        cc.director.getCollisionManager().enabled = true;
    },

    start () {
        this.initEvent();
    },

    initEvent(){
        this.node.on(cc.Node.EventType.TOUCH_START, this.uiNodeStartEvent, this);
        this.node.on(cc.Node.EventType.TOUCH_MOVE, this.uiNodeMoveEvent, this);
        this.node.on(cc.Node.EventType.TOUCH_END, this.uiNodeEndEvent, this);
        this.node.on(cc.Node.EventType.TOUCH_CANCEL, this.uiNodeEndEvent, this);
    },

    uiNodeStartEvent(e) {
        this.touchStartLocal = null;
        this.touchMoveLocal = null;
        this.touchStartLocal = this.node.convertToNodeSpaceAR(e.getLocation());
        //初始化切割线的位置和长度
        this.line.setPosition(this.touchStartLocal);
        this.line.width = 0;
    
    },
    //
    uiNodeMoveEvent(e) {
        if (!this.touchStartLocal) return;
        console.log("移动");
        this.touchMoveLocal = this.node.convertToNodeSpaceAR(e.getLocation());
        //计算切割线旋转角度 和长度
        this.line.angle = -this.rotationTarget(this.touchStartLocal, this.touchMoveLocal) + 90;
        this.line.width = Math.abs(this.line.getPosition().sub(this.touchMoveLocal).mag());
    },
    
    //
    uiNodeEndEvent(e) {
        if (!this.touchStartLocal) return;
        let touchEndLocal = this.node.convertToNodeSpaceAR(e.getLocation());
        let touchStartLocal = this.node.convertToNodeSpaceAR(e.getStartLocation());
        this.line.width = 0;
    },

    // update (dt) {},
});
