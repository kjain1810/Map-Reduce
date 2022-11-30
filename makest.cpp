#include <bits/stdc++.h>

using namespace std;

int main(){
    for(int s_size = 10000; s_size<=2560000; s_size*=4){
        for(int t_size = 10; t_size<=s_size; t_size*=4){
            stringstream filename_stream;
            filename_stream<<"/data0/kunal2/testcases/raw/raw_"<<s_size<<"_"<<t_size;
            string filename;
            filename_stream>>filename;
            ofstream file;
            file.open(filename);
            for(int i=0; i<s_size; i++){
                file<<(char)('a'+random()%2);
            }
            file<<endl;
            for(int i=0; i<t_size; i++){
                file<<(char)('a'+random()%2);
            }
            file.close();
        }
    }
}