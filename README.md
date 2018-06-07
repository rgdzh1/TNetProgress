# TNetProgress
###  Hello,这是一个文件上传、下载的进度监听库
  使用简单.无论okhttp还是retrofit方式,只需要在oKhttpClient的构建者对象中添加拦截器即可.<br>
  下载拦截器:DownloadInterceptor.<br>
  上传拦截器:UploadInterceptor.<br>
###  提示:
  Demo细致演示了使用方式.<br> 
  为了不添加冗余代码,进度数据变化体现在Button文本上,更直观是进度条.<br>
###  预览图:
  <img width="350"  src="https://github.com/HoldMyOwn/TNetProgress/blob/master/preview/tnetprogress-6.gif" /><br>
###  用法:
<pre>

     //添加下载拦截器(this参数是实现下载进度接口的对象) 
     mDownClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new DownloadInterceptor(this))
                .build();
                
     //添加上传拦截器(this参数是实现上传回调接口的对象)            
     mUploadClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new UploadInterceptor(this))
                .build();
                
 </pre>
###   事件回调:
####  下载回调:
<pre>

    /**
     * 下载进度信息更新回调
     *
     * @param info 下载进度信息
     */
    void onDownLoadProgress(ProgressInfo info);

    /**
     *如果该方法回调,那么下载进度信息更新回调则不执行
     * @param info 下载进度信息
     */
    void onDownLoadGetContentLengthFail(ProgressInfo info);
    
</pre>    
####  上传回调:
<pre>

      /**
     * 上传进度信息更新回调
     *
     * @param info 上传进度信息
     */
    void onUpLoadProgress(ProgressInfo info);

    /**
     * 获取上传字节总长度失败
     * 如果该方法回调,那么上传进度信息更新回调则不执行
     *
     *
     * @param info 上传进度信息
     */
    void onUploadGetContentLengthFail(ProgressInfo info);
    
</pre>

###   具体细节用法,下载查看Demo
###   模板依赖:&nbsp;&nbsp;项目里面的tnetprogress模板(可更加灵活扩展)
###   gradle依赖:&nbsp;&nbsp;&nbsp;compile&nbsp;'com.jkt:tnetprogress:1.0.4'

