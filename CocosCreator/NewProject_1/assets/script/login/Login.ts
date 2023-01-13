
const {ccclass, property} = cc._decorator;

import LoginUtils = require("./LoginUtils") ;

@ccclass
export default class Login extends cc.Component {
    // LIFE-CYCLE CALLBACKS:

    // onLoad () {}

    start () {
       
    }

    login(){
    
        console.log("=========================login ")
        // jsb.reflection.callStaticMethod("org/cocos2dx/javascript/JavaScriptCall", "test", "(Ljava/lang/String;II)V","hello",2, 3);
        jsb.reflection.callStaticMethod("org/cocos2dx/javascript/JavaScriptCall", "lineLogin", "(Ljava/lang/String;)V","hello");
        // let loginUtil =  new LoginUtils();
        // LoginUtils.login();
    }

    public static CocosFun(msg:string){
        console.log(msg);
    }

    // update (dt) {}
}
