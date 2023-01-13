
const {ccclass, property} = cc._decorator;

@ccclass
export default class NewClass extends cc.Component {

    @property
    speed: number = 20;

    @property
    width: number = 288;

    // LIFE-CYCLE CALLBACKS:

    onLoad () {

    }

    start () {
        
    }

    update (dt) {
        for (let bg of this.node.children) {
            if(bg.x < -this.width ){
                bg.x  = this.width-1
            }
            bg.x -= this.speed * dt
        }
    }
}
