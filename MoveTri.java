/*  
   improve MoveTri
   so can change the scene data
   interactively
*/

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

// import static org.lwjgl.glfw.Callbacks.*;
 import static org.lwjgl.glfw.GLFW.*;   // just for the key constants
// import static org.lwjgl.system.MemoryUtil.*;

public class MoveTri extends Basic {

  public static void main(String[] args) {

    MoveTri app = new MoveTri( "Exercise 2 by Andres Restrepo", 650, 650, 30 );
    app.start();

    
  }// main

  // instance variables 

  private FloatBuffer backColor;
  private Shader v1, f1;
  private int hp1;  // handle for the GLSL program

  private int vao;  // handle to the vertex array object

  private int positionHandle, colorHandle;
  private FloatBuffer positionBuffer, colorBuffer;
  private GameEngine engine = new GameEngine();
  
  // scene data
  private int numTris = 4;
  private float[] positionData = engine.positionData;
  private float[] colorData = engine.colorData;

  // construct basic application with given title, pixel width and height
  // of drawing area, and frames per second
  public MoveTri( String appTitle, int pw, int ph, int fps )
  {
    super( appTitle, pw, ph, (long) ((1.0/fps)*1000000000) );
    // do other stuff before OpenGL starts up

    numTris = positionData.length / 3;
    vao = -1;
  }

  protected void init()
  {
    String vertexShaderCode =
"#version 330 core\n"+
"layout (location = 0 ) in vec3 vertexPosition;\n"+
"layout (location = 1 ) in vec3 vertexColor;\n"+
"out vec3 color;\n"+
"void main(void)\n"+
"{\n"+
"  color = vertexColor;\n"+
"  gl_Position = vec4( vertexPosition, 1.0);\n"+
"}\n";

    System.out.println("Vertex shader:\n" + vertexShaderCode + "\n\n" );

    v1 = new Shader( "vertex", vertexShaderCode );

    String fragmentShaderCode =
"#version 330 core\n"+
"in vec3 color;\n"+
"layout (location = 0 ) out vec4 fragColor;\n"+
"void main(void)\n"+
"{\n"+
"  fragColor = vec4(color, 1.0 );\n"+
"}\n";

    System.out.println("Fragment shader:\n" + fragmentShaderCode + "\n\n" );

    f1 = new Shader( "fragment", fragmentShaderCode );

    hp1 = GL20.glCreateProgram();
         Util.error("after create program");
         System.out.println("program handle is " + hp1 );

    GL20.glAttachShader( hp1, v1.getHandle() );
         Util.error("after attach vertex shader to program");

    GL20.glAttachShader( hp1, f1.getHandle() );
         Util.error("after attach fragment shader to program");

    GL20.glLinkProgram( hp1 );
         Util.error("after link program" );

    GL20.glUseProgram( hp1 );
         Util.error("after use program");

    // set background color to white
    backColor = Util.makeBuffer4( 1.0f, 1.0f, 1.0f, 1.0f );

    // set up stuff for the vertex data that can be done just once:

    // create vertex buffer objects and their handles one at a time
    positionHandle = GL15.glGenBuffers();
    colorHandle = GL15.glGenBuffers();
    System.out.println("have position handle " + positionHandle +
                       " and color handle " + colorHandle );

    // create these buffers once to be reused repeatedly in sendData
    // (unimportant that the data is being copied in, just to get size right)
    positionBuffer = Util.arrayToBuffer( positionData );
    colorBuffer = Util.arrayToBuffer( colorData );

    sendData();

  }

  protected void processInputs()
  {
    // process all waiting input events
    while( InputInfo.size() > 0 )
    {
      InputInfo info = InputInfo.get();

      if( info.kind == 'k' && (info.action == GLFW_PRESS || 
                               info.action == GLFW_REPEAT) )
      {
        int code = info.code;

        if ( code == GLFW_KEY_LEFT ) {
           engine.moveLeft();
        }
        else if ( code == GLFW_KEY_RIGHT ) {
           engine.moveRight();
        }
        else if ( code == GLFW_KEY_DOWN ) {
           engine.moveDown();
        }
        else if ( code == GLFW_KEY_UP ) {
           engine.moveUp();
        }

      }// input event is a key

      else if ( info.kind == 'm' )
      {// mouse moved
      //  System.out.println( info );
      }

      else if( info.kind == 'b' )
      {// button action
       //  System.out.println( info );
      }

    }// loop to process all input events

  }

  protected void update()
  {
  }

  protected void display()
  {
System.out.println( getStepNumber() );

    GL30.glClearBufferfv( GL11.GL_COLOR, 0, backColor );

     sendData();

    // activate vao
    GL30.glBindVertexArray( vao );
           Util.error("after bind vao");

    // draw the buffers
    GL11.glDrawArrays( GL11.GL_TRIANGLES, 0, numTris );
           Util.error("after draw arrays");

      // apparently is good habit to deselect things
      GL20.glDisableVertexAttribArray(0);
    
   }

  // do all the stuff to map position and color data
  // to vao
  private void sendData() {

    // connect data to the VBO's
    
     sendArrayToBuffer( positionData, positionBuffer );
     sendArrayToBuffer( colorData, colorBuffer );
Util.showBuffer("position buffer: ", positionBuffer );  positionBuffer.rewind();
Util.showBuffer("color buffer: ", colorBuffer );  colorBuffer.rewind();

       // now connect the buffers
       GL15.glBindBuffer( GL15.GL_ARRAY_BUFFER, positionHandle );
             Util.error("after bind positionHandle");
       GL15.glBufferData( GL15.GL_ARRAY_BUFFER, 
                                     positionBuffer, GL15.GL_STATIC_DRAW );
             Util.error("after set position data");
       GL15.glBindBuffer( GL15.GL_ARRAY_BUFFER, colorHandle );
             Util.error("after bind colorHandle");
       GL15.glBufferData( GL15.GL_ARRAY_BUFFER, 
                                     colorBuffer, GL15.GL_STATIC_DRAW );
             Util.error("after set color data");

    // set up vertex array object

      // delete previous handle and binding
      // before doing a new one
      if ( vao != -1 ) {
         GL30.glBindVertexArray(0);
         GL30.glDeleteVertexArrays( vao );
      }

      // using convenience form that produces one vertex array handle
      vao = GL30.glGenVertexArrays();
           Util.error("after generate single vertex array");
      GL30.glBindVertexArray( vao );
           Util.error("after bind the vao");
      System.out.println("vao is " + vao );

      // enable the vertex array attributes
      GL20.glEnableVertexAttribArray(0);  // position
             Util.error("after enable attrib 0");
      GL20.glEnableVertexAttribArray(1);  // color
             Util.error("after enable attrib 1");
  
      // map index 0 to the position buffer index 1 to the color buffer
      GL15.glBindBuffer( GL15.GL_ARRAY_BUFFER, positionHandle );
             Util.error("after bind position buffer");
      GL20.glVertexAttribPointer( 0, 3, GL11.GL_FLOAT, false, 0, 0 );
             Util.error("after do position vertex attrib pointer");

      // map index 1 to the color buffer
      GL15.glBindBuffer( GL15.GL_ARRAY_BUFFER, colorHandle );
             Util.error("after bind color buffer");
      GL20.glVertexAttribPointer( 1, 3, GL11.GL_FLOAT, false, 0, 0 );
             Util.error("after do color vertex attrib pointer");

   }// sendData

   // given an array with data in it and an allocated buffer,
   // overwrite buffer contents with array data
   private void sendArrayToBuffer( float[] array, FloatBuffer buffer ) {
      buffer.rewind();
      for (int k=0; k<array.length; k++) {
         buffer.put( array[k] );
      }
   }

}// MoveTri