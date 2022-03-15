
# pip install torch==1.10.0+cu113 torchvision==0.11.1+cu113 torchaudio===0.10.0+cu113 -f https://download.pytorch.org/whl/cu113/torch_stable.html

# !pip install "opencv-python-headless<4.3"

# !pip install ArabicOcr

from pickletools import uint8
from ArabicOcr import arabicocr
import cv2
from google.colab.patches import cv2_imshow
from PIL import Image
import base64
import io
import numpy as np


def main(data):
	decodeImage=Base64.b64decode(data)
	npData=np.fromstring(decodeImage,uint8)
	img=cv2.imdecode(npData,cv2.IMREAD_UNCHANGED)
	pil_img=Image.fromarray(img)
	buff=io.BytesIO()
	pil_img.save(buff,format='PNG')

	image_path=pil_img
	out_image='out.jpg'
	results=arabicocr.arabic_ocr(image_path,out_image)
	print(results)
	words=[]
	for i in range(len(results)):	
			word=results[i][1]
			words.append(word)
	with open ('file.txt','w',encoding='utf-8')as myfile:
			myfile.write(str(words))

	img = cv2.imread('out.jpg', cv2.IMREAD_UNCHANGED)
	cv2_imshow(img)
	cv2.waitKey(0)

	return words
