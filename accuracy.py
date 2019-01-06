import json
import tinydb
from tinydb import  where,Query

def load_json(file_path):
	fh = open(file_path,'r')
	data = json.load(fh)
	fh.close()
	return data

def test_func(val, texts,data_a):
    return val==texts
def compare_json_data(data_a,parent_name):
    if (type(data_a) is list):
        for list_item in data_a:
            compare_json_data(list_item,parent_name)
    elif (type(data_a) is dict):
        for dict_key,dict_value in data_a.items():
            if dict_key =='featureName' or dict_key== 'srvcText' or dict_key=='exclsnName':
               print(dict_value+'-->'+str(db.contains(Query().data.any(where(parent_name).any(where(dict_key).test(test_func,dict_value,data_a))))))
            if (type(dict_value) is list):
                if(dict_key =='medicalServices' or dict_key =='planFeatures'):
                    print('No of ',dict_key,'->',len(dict_value))
            compare_json_data(dict_value,dict_key)

db = tinydb.TinyDB("mydb0000004.json")

# json1=load_json('D://JSON//Test.json')
# db.insert(json1)
a_json = load_json('D://JSON//Test.json')
compare_json_data(a_json,'')
