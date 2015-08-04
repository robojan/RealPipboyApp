#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform vec2 u_clippingTL;
uniform vec2 u_clippingBR;
uniform sampler2D u_texture;


void main()
{
	if((gl_FragCoord.x > u_clippingTL.x && gl_FragCoord.y < u_clippingTL.y &&
		gl_FragCoord.x < u_clippingBR.x && gl_FragCoord.y > u_clippingBR.y))
	{
		gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
	}
}
