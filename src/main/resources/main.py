
from os import path
from tensorflow.keras.models import load_model 
from PIL import Image
from PIL import ImageFont
from PIL import ImageDraw 
from tensorflow.keras.preprocessing import image
import numpy as np
from platform import system
import sys

OSname = system()
DEFAULT_DICT = {"Windows":"start", "Linux":"see", "Darwin":"open"}
DEFAULT_APP = DEFAULT_DICT[OSname]


def calculate(imgName):
    model = load_model('fashion_mnist_dense.h5')
    img = image.load_img(imgName, target_size=(28, 28), color_mode = "grayscale")
    x = image.img_to_array(img)
    x = x.reshape(1, 784)
    x = 255 - x
    x /= 255
    prediction = model.predict(x)
    prediction = np.argmax(prediction)
    return (prediction)
    

def main():
    imgName = sys.argv[1]
    print(imgName)
    while not path.exists(imgName):
        imgName = input("The image does not exist. Please enter a valid image filename: ")
    classes = ['футболка', 'брюки', 'свитер', 'платье', 'пальто', 'туфли', 'рубашка', 'кроссовки', 'сумка', 'ботинки']
    num = calculate(imgName)
    result = classes[num]
    
    newImage = Image.new('RGB', (200,50), color = 'white')
    
    draw = ImageDraw.Draw(newImage)
    font = ImageFont.truetype("Arial Bold.ttf", 32, encoding='UTF-8')
    draw.text((0, 0),result,(0,0,0), font=font) # this will draw text with Blackcolor and 16 size
    newImage.save(sys.argv[2])
    print(result)

main()