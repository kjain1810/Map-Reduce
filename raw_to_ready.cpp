#include <bits/stdc++.h>

using namespace std;

vector<string> S, T;


int main(){
    for(int s_size = 10000; s_size<=2560000; s_size*=4){
        for(int t_size = 10; t_size<=s_size/2; t_size*=4){
            stringstream filename_stream, filename_stream2;
            filename_stream<<"/data0/kunal2/testcases/raw/raw_"<<s_size<<"_"<<t_size;
            filename_stream2<<"/data0/kunal2/testcases/ready/ready_"<<s_size<<"_"<<t_size;
            string filename;
            filename_stream>>filename;
            ifstream file;
            ofstream file2;
            file.open(filename);
            string s, t;
            file>>s;
            file>>t;
            cout<<s_size<<endl;
            cout<<t_size<<endl;
            file.close();
            S.clear();
            T.clear();
            for(int i=0; i<s_size; i+=10000){
                string tmp="";
                for(int j=i; j<min(i+10000+9, s_size); j++)
                    tmp+=s[j];
                S.push_back(tmp);
            }
            for(int i=0; i<t_size; i+=10){
                string tmp="";
                for(int j=i; j<min(i+10, t_size); j++)
                    tmp+=t[j];
                T.push_back(tmp);
            }
            filename_stream2>>filename;
            file2.open(filename);
            for(int i=0; i<S.size(); i++){
                for(int j=0; j<T.size(); j++){
                    file2<<S[i]<<" "<<T[j]<<" "<<i*10000<<" "<<j*10<<endl;
                }
            }
            file2.close();
        }
    }
}