import json
import tinydb
from tinydb import  where,Query
from difflib import SequenceMatcher

def load_json(file_path):
	fh = open(file_path,'r')
	data = json.load(fh)
	fh.close()
	return data
def object_accuracy(obj1,obj2):
    list_accuracy=0.0
    dict_accuracy=0.0
    if type(obj1) and type(obj2) is list:
        if len(obj1)==len(obj2):
            for list_item1,list_item2 in obj1,obj2:
                list_accuracy=list_accuracy+object_accuracy(list_item1,list_item2)
            list_accuracy=list_accuracy/len(obj1)
    elif type(obj1) and type(obj2) is dict:
        for dict_key,dict_value in obj1.items():
            dict_accuracy=dict_accuracy+object_accuracy(dict_value,obj2.get(dict_key))
        dict_accuracy=dict_accuracy/len(obj1.items())
    else:
        return SequenceMatcher(None, obj1, obj2).ratio()
    if list_accuracy!=0.0 and dict_accuracy!=0.0:
        return (list_accuracy+dict_accuracy)/2
    else:
        return (list_accuracy+dict_accuracy)
def test_func(val, texts,data_a):
    excl_db=[x for x in val if 0.9<=SequenceMatcher(None, texts, x.get('featureName')).ratio() <=1 ]
    if not excl_db:       
        return 1==0
    else:
        print(object_accuracy(excl_db[0],data_a))
        return 'Found'
def compare_json_data(data_a,parent_name):
    if (type(data_a) is list):
        for list_item in data_a:
            compare_json_data(list_item,parent_name)
    elif (type(data_a) is dict):
        for dict_key,dict_value in data_a.items():
            if dict_key=='featureName':
               print(dict_value+'-->'+str(db.contains(Query().data.any(where(parent_name).test(test_func,dict_value,data_a)))))
            if (type(dict_value) is list):
                if(dict_key =='medicalServices' or dict_key =='planFeatures'):
                    print('No of ',dict_key,'->',len(dict_value))
            compare_json_data(dict_value,dict_key)
 
db = tinydb.TinyDB("mydb00000089237.json")
Test
json1=load_json('D://JSON//Test.json')
db.insert(json1)
a_json = load_json('D://JSON//Test.json')
compare_json_data(a_json,'')
