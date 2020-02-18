import java.util.ArrayList;

public class GameEngine
{
    private ReadFile reader;
    private ArrayList<Box> boxes;
    float positionData[];
    float colorData[];
    Box nullBox, currentBox; 

    public GameEngine(){
        boxes = new ArrayList<Box>();
        buildBoxes();
        int size = boxes.size()*18;
        positionData = new float[size];
        colorData = new float [size];   
        update();                 
    }

    private void buildBoxes(){
        ArrayList<Double> data;
        nullBox = new Box (10,10,0,0,0,0,0);
        reader = new ReadFile();
        data = reader.dataList();
        for (int i =1 ; i < data.size(); i = i+7){
            currentBox = new Box(data.get(i), 
                                data.get(i+1),
                                data.get(i+2),  
                                data.get(i+3),
                                data.get(i+4),
                                data.get(i+5),
                                data.get(i+6));
            boxes.add(currentBox);
        }
        boxes.add(nullBox);     
    }

    private void updatePositionData(){
        for (int i = 0; i < boxes.size(); i++){
            float coordinates [] = boxes.get(i).getPositionData();
            for (int j =0; j < coordinates.length; j++){
                this.positionData[j+18*i] = coordinates [j];
            }
        }    
    }

    private void updateColorData(){     
        for (int i = 0; i < boxes.size(); i++){
            float coordinates [] = boxes.get(i).getColorData();
            for (int j =0; j < coordinates.length; j++){
                this.colorData[j+18*i] = coordinates [j];
            }
        }   
    }

    private void update(){
        updatePositionData();
        updateColorData();
    }

    private void endGame(){
        System.exit(0);
    }

    private int greenBoxesCounter(){
        int amountGreenBoxes = 0;
        for (int i = 0; i < boxes.size(); i++){
            if (boxes.get(i).boxType == "Food"){
                amountGreenBoxes++;
            }
        }
        return amountGreenBoxes;
    }

    public void moveLeft(){
        boxes.get(0).moveLeft();
        update();
        if (collisionType()){moveRight();}
    }

    public void moveRight(){
        boxes.get(0).moveRight();
        update();
        if (collisionType()){moveLeft();}
    }

    public void moveDown(){
        boxes.get(0).moveDown(); 
        update();
        if (collisionType()){moveUp();}
    }

    public void moveUp(){
        boxes.get(0).moveUp();
        update();
        if (collisionType()){moveDown();}
    }

    public boolean collisionType(){
        int collidedBox = collision();
        String boxType = boxes.get(collidedBox).boxType;
        boolean wall = false;
        if (boxType == "Poison" && collidedBox != 0){
            endGame();
            }
        else if (boxType == "Food" && collidedBox != 0){
            boxes.remove(collidedBox);
            if (greenBoxesCounter() == 0){
                endGame();}
            update();
            }
        else if (boxType == "Wall" && collidedBox != 0){
            wall = true;
            }
        return wall;
    }
        

    public int collision(){
        boolean collision = false;
        int boxIndex = 0;
        for (int i=1; i < boxes.size() && !collision; i++){      
            collision = boxes.get(0).collision(boxes.get(i));
            if (collision){ boxIndex = i;}     
        }
        return boxIndex;
    }

}


