#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTexCoord;
uniform samplerExternalOES sTexture;
void main(){
    //gl_FragColor=vec4(0,0.5,0.5,1);
    gl_FragColor=texture2D(sTexture,vTexCoord);
}