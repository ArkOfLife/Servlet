from collections import defaultdict
import logging
class AccuracyLogger:
    accuracy = 0
    d = defaultdict(list)
    def __init__(self):
        self.accuracy=0
        self.d=defaultdict(list)

    def write_error(self,text):
        for key,value in self.d.items():
            logging.error('********************************')
            logging.error(text)
            logging.error('********************************')
            logging.error('Field Name:' + key)
            logging.error('Accuracy:' + str(self.accuracy))
            for item in value:
                logging.error(item)
def create_log_file(filename):
    logging.basicConfig(filename=filename, format='%(message)s')