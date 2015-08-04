#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying vec2 v_texCoords;
varying vec4 v_color;

uniform sampler2D u_texture;

uniform vec2 u_screenSize;
uniform float u_interlacePeriod;
uniform float u_interlaceAmplitude;
uniform vec4 u_pipboyColor;
uniform float u_sweepPosition;
uniform float u_sweepLength;
uniform float u_sweepIntensity;

const float pi = 3.14159265;

void main() {
    vec4 color = u_pipboyColor * v_color * texture2D(u_texture, v_texCoords);

    // Normalize screen coords
    vec3 screenPos = vec3(gl_FragCoord.xy/u_screenSize, gl_FragCoord.z);

    float interlacing = (1.0 - u_interlaceAmplitude) + u_interlaceAmplitude *
        sin((screenPos.y / u_interlacePeriod) * 2.0 * pi);

    float sweepPos = screenPos.y - u_sweepPosition;
    float sweep;
    if(sweepPos > 0.0) {
        sweep = (u_sweepIntensity * exp(-sweepPos/u_sweepLength));
    } else {
        sweep = 0.0;
    }

    color.rgb = interlacing * color.rgb;
    color += sweep * u_pipboyColor;

    gl_FragColor = color;
}