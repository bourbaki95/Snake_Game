public class Box
{
    double xCorner, yCorner, width, height, red, green, blue;
    double rightEdge, leftEdge, topEdge, bottomEdge; 
    String boxType;

    public Box(double xCorner, double yCorner, double width, double height, double red, double green, double blue)
    {   this.xCorner = xCorner;
        this.yCorner = yCorner;
        this.width = width;
        this.height = height;
        this.red = red;
        this.green = green;
        this.blue = blue;    
        this.setEdges();
        this.setColor();
    }

    private void setEdges(){
        this.rightEdge = this.xCorner + this.width;
        this.leftEdge = this.xCorner;
        this.topEdge = this.yCorner + this.height;
        this.bottomEdge = this.yCorner;
    }

    private void setColor()
    {
        if (this.red == 1 && this.green == 0 && this.blue == 0)
            {this.boxType = "Poison";}
        else if(this.red == 0 && this.green == 1 && this.blue == 0)
            {this.boxType = "Food";}
        else        
            {this.boxType = "Wall";}
    }

    public float[] getPositionData(){
        float [] positionData = {(float)this.xCorner, (float)this.yCorner, 0.0f,
                                 (float)this.rightEdge, (float)this.yCorner, 0.0f,
                                 (float)this.rightEdge, (float)this.topEdge, 0.0f,
                                 (float)this.xCorner, (float)this.yCorner, 0.0f,
                                 (float)this.xCorner, (float)this.topEdge, 0.0f,
                                 (float)this.rightEdge, (float)this.topEdge, 0.0f,
                                };
        return positionData;
    }

    public float[] getColorData(){
        float [] colorData =    {(float)this.red, (float)this.green, (float)this.blue,
                                (float)this.red, (float)this.green, (float)this.blue,
                                (float)this.red, (float)this.green, (float)this.blue,
                                (float)this.red, (float)this.green, (float)this.blue,
                                (float)this.red, (float)this.green, (float)this.blue,
                                (float)this.red, (float)this.green, (float)this.blue,
                                };
        return colorData;
    }

    public void moveLeft(){      
        this.xCorner   -= 0.01;   
        this.rightEdge -= 0.01;   
        this.rightEdge -= 0.01;   
        this.xCorner   -= 0.01;  
        this.xCorner   -= 0.01;  
        this.rightEdge -= 0.01;   
        this.setEdges();
    }

    public void moveRight(){      
        this.xCorner   += 0.01;   
        this.rightEdge += 0.01;   
        this.rightEdge += 0.01;   
        this.xCorner   += 0.01;  
        this.xCorner   += 0.01;  
        this.rightEdge += 0.01;  
        this.setEdges();  
    }

    public void moveDown(){
        this.yCorner -= 0.01;
        this.yCorner -= 0.01;
        this.topEdge -= 0.01;
        this.yCorner -= 0.01;
        this.topEdge -= 0.01;
        this.topEdge -= 0.01;
        this.setEdges();
    }

    public void moveUp(){
        this.yCorner += 0.01;
        this.yCorner += 0.01;
        this.topEdge += 0.01;
        this.yCorner += 0.01;
        this.topEdge += 0.01;
        this.topEdge += 0.01;
        this.setEdges();
       
    }

    public boolean collision(Box box)
    {   boolean horizontalCollision = this.rightEdge > box.leftEdge && this.leftEdge < box.rightEdge;
        boolean verticalCollsion = this.bottomEdge < box.topEdge && this.topEdge > box.bottomEdge;
        return horizontalCollision && verticalCollsion;
    }


}