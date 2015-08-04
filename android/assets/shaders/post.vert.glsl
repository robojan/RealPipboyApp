
attribute vec2 a_position;
attribute vec2 a_texCoords;

varying vec2 v_texCoords;
varying vec4 v_color;
void main() {
    gl_Position = vec4(a_position, 0, 1.0);
    v_texCoords = a_texCoords;
    v_color = vec4(1,1,1,1);
}