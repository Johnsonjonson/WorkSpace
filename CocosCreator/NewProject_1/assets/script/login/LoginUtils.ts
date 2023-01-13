
export class LoginUtils {
    public static login(){
        cc.director.on("TestDataResultEvent",(msg:string)=>{
            console.log(msg);
        })
        jsb.reflection.callStaticMethod("org/cocos2dx/javascript/AppActivity", "AndroidFun", "(Ljava/lang/String;)Ljava/lang/String;", "hello");
    }

    public static CocosFun(msg:string){
        console.log(msg);
    }

    
    // window['tsFunc'] = (arg1, arg2) => {
    //     console.log(arg1);
    //     console.log(arg2);
    // };
}
