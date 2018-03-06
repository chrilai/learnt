// ParabolicSARTest.cpp
//
#include <array>
#include <iomanip>
#include <iostream>
#include "ParabolicSAR.h"
using namespace std;

const string INPUT_FOLDER = "historical_data\\";
const string OUTPUT_FOLDER = "results\\";
const string FILETYPE_EXTENSION = ".csv";
const size_t NUM_STOCKS = 10;
const array<string, NUM_STOCKS> SYMBOLS = { "AZO",
											  	  	  	  "PHYS",
											  	  	  	  "JJP",
											  	  	  	  "CNP",
											  	  	  	  "DBP",
											  	  	  	  "ORLY",
											  	  	  	  "COST",
											  	  	  	  "DGL",
											  	  	  	  "ASPS",
											  	  	  	  "CVS" };

//***************************************************************************
// string testResult( string testResult, T expected, T result )
// Purpose: Compare test result with expected value and return a PASS or FAIL
//***************************************************************************
template<typename T>
string testResult(T expected, T result)
{
	string testResult;
	if (expected == result)
	{
		testResult = "PASS";
	}
	else
	{
		testResult = "FAIL";
		cout << '\n' << "Expected: " << expected << endl;
		cout << "Result: " << result << endl;
	} // end if
	return testResult;
} // end testResult(T expected, T result)

//***************************************************************************
// void test( ParabolicSAR::Point p, ParabolicSAR pSAR )
// Purpose: Perform testing on data Point object and file input/output
//***************************************************************************
void test()
{
	cout << "Testing" << '\n' << endl;

	cout << "Point Values" << endl;
	ParabolicSAR::Point p = ParabolicSAR::Point(boost::gregorian::date(2014,12,15),
															  1.0, 2.0, 3.0, 4.0, 5, 4.1);
	cout << "date: " << testResult(boost::gregorian::date(2014,12,15), p.date) << endl;
	cout << "open: " << testResult(1.0, p.open) << endl;
	cout << "high: " << testResult(2.0, p.high) << endl;
	cout << "low: " << testResult(3.0, p.low) << endl;
	cout << "close: " << testResult(4.0, p.close) << endl;
	cout << "volume: " << testResult(5, p.volume) << endl;
	cout << "adjclose: " << testResult(4.1, p.adjclose) << endl;
	cout << "sar: " << testResult(0.0, p.sar) << endl;
	cout << "ep: " << testResult(0.0, p.ep) << endl;
	cout << "trend: " << testResult(false, p.trend) << endl;
	cout << "af: " << testResult(0.0, p.af) << endl;
	cout << endl;

	cout << "SAR Data I/O Tests" << endl;
	ParabolicSAR pSAR = ParabolicSAR();
	pSAR.printSAR();
	pSAR.writeCSV(OUTPUT_FOLDER + SYMBOLS.front() + FILETYPE_EXTENSION);
	pSAR.readCSV(INPUT_FOLDER + SYMBOLS.front() + FILETYPE_EXTENSION);
	pSAR.printSAR();
	pSAR.writeCSV(OUTPUT_FOLDER + SYMBOLS.front() + FILETYPE_EXTENSION);
	cout << endl;
} // end test()

int main()
{
	// Perform Testing
	test();

	// Demonstrate Assignment Example
	cout << "Parabolic SAR Results" << '\n' << endl;
	for(auto it = SYMBOLS.begin(); it != SYMBOLS.end(); ++it)
	{
		cout << "Stock Symbol: " << *it << endl;
		ParabolicSAR pSAR = ParabolicSAR();
		pSAR.readCSV(INPUT_FOLDER + *it + FILETYPE_EXTENSION);
		pSAR.writeCSV(OUTPUT_FOLDER + *it + FILETYPE_EXTENSION);
		pSAR.printSAR();
		cout << endl;
	} // end for

	return 0;
}
