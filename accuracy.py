import json
import tinydb
from tinydb import where, Query
from difflib import SequenceMatcher
from collections import defaultdict
from accuracy_logger import AccuracyLogger
from accuracy_logger import create_log_file

def load_json(file_path):
    fh = open(file_path, 'r')
    data = json.load(fh)
    fh.close()
    return data


currentlog = None
log = None


def object_accuracy(obj1, obj2):
    global currentlog
    list_accuracy = 0.0
    dict_accuracy = 0.0
    if type(obj1) and type(obj2) is list:
        if len(obj1) == len(obj2):
            for list_item1, list_item2 in zip(obj1, obj2):
                list_accuracy = list_accuracy + object_accuracy(list_item1, list_item2)
            list_accuracy = list_accuracy / len(obj1)
    elif type(obj1) and type(obj2) is dict:
        for dict_key, dict_value in obj1.items():
            obj_acc = object_accuracy(dict_value, obj2.get(dict_key))
            if obj_acc < 1:
                if isinstance(obj2.get(dict_key),str):
                    currentlog.d[dict_key].append('Current Value :' + obj2.get(dict_key))
                    currentlog.d[dict_key].append('Similar Value :' + dict_value)
            dict_accuracy = dict_accuracy + obj_acc
        dict_accuracy = dict_accuracy / len(obj1.items())
    else:
        return SequenceMatcher(None, obj1, obj2).ratio()
    if list_accuracy != 0.0 and dict_accuracy != 0.0:
        return (list_accuracy + dict_accuracy) / 2
    else:
        return (list_accuracy + dict_accuracy)


def test_func(val, texts, data_a):
    global currentlog, log
    if 'srvcCtgry' in val[0]:
        excl_db = [x for x in val if 0.9 <= (SequenceMatcher(None, data_a.get('srvcCtgry'), x.get('srvcCtgry')).ratio() +  SequenceMatcher(None, texts, x.get('srvcText')).ratio())/2 ]
    elif 'exclsnName' in val[0]:
        excl_db = [x for x in val if 0.9 <= SequenceMatcher(None, texts, x.get('exclsnName')).ratio()]
    else:
        excl_db = [x for x in val if 0.9 <= SequenceMatcher(None, texts, x.get('srvcText')).ratio()]
    if not excl_db:
        return 1 == 0
    else:
        acc=object_accuracy(excl_db[0], data_a)
        if acc < 1:
            currentlog.accuracy =acc
        if log.accuracy < currentlog.accuracy:
            log = currentlog
        currentlog=AccuracyLogger()
        return 1 == 0


def compare_json_data(data_a, parent_name):
    global currentlog,log
    if (type(data_a) is list):
        for list_item in data_a:
            compare_json_data(list_item, parent_name)
    elif (type(data_a) is dict):
        for dict_key, dict_value in data_a.items():
            if dict_key == 'exclsnName':
                currentlog = AccuracyLogger()
                log=AccuracyLogger()
                print(dict_value + '-->' + str(
                    db.contains(Query().data.any(where(parent_name).test(test_func, dict_value, data_a)))))
                log.write_error(dict_value)
            if (type(dict_value) is list):
                if (dict_key == 'medicalServices' or dict_key == 'planFeatures'):
                    print('No of ', dict_key, '->', len(dict_value))
            compare_json_data(dict_value, dict_key)


db = tinydb.TinyDB("mydb00000089237.json")

json1=load_json('D://JSON//Test.json')
create_log_file('Test.log')
db.insert(json1)
create_log_file('Test.log')
a_json = load_json('D://JSON//Test.json')
compare_json_data(a_json, '')
