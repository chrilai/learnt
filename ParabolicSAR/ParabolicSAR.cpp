// ParabolicSAR.cpp
//
#include <algorithm>
#include <cstdlib>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <boost/foreach.hpp>
#include <boost/tokenizer.hpp>
#include "ParabolicSAR.h"
using namespace std;

const std::string ParabolicSAR::DELIMITER = ","; // delimiter
const std::string ParabolicSAR::HEADER =
	"Date,Open,High,Low,Close,Volume,Adj Close,SAR,EP,Trend,AF"; // header row
const boost::char_separator<char> ParabolicSAR::SEPARATOR(","); // separator
const std::locale ParabolicSAR::DATE_FORMAT(std::locale::classic(),
	new boost::gregorian::date_facet("%Y-%m-%d")); // date format
const std::locale ParabolicSAR::INPUT_DATE_FORMAT(std::locale::classic(),
	new boost::gregorian::date_input_facet("%Y-%m-%d")); // date format

//***************************************************************************
// void readCSV( string filename, vector<Point> data )
// Purpose: Read historical stock price data from CSV file
// Postcondition: Data values are stored in vector
// Input: (file stream) Stock price data
//***************************************************************************
void ParabolicSAR::readCSV(string filename)
{
	ifstream csvFile(filename); // open file
	if (csvFile)
	{
		cout << "Read file: \"" << filename << "\"" << endl;
		string header;
		string row;
		string dateString;
		boost::gregorian::date date;
		double open;
		double high;
		double low;
		double close;
		int volume;
		double adjclose;

		this->data.clear();
		getline(csvFile, header); // read header row
//		clog << header << endl;
		while (getline(csvFile, row)) // read data rows
		{
			// read each cell in row
			std::stringstream ss;
			boost::tokenizer<boost::char_separator<char>> tokenizer(row, SEPARATOR);
			BOOST_FOREACH(string cell, tokenizer)
			{
				ss << cell << " ";
			}
			ss >> dateString >> open >> high >> low >> close >> volume >> adjclose;
			// parse date string
			std::istringstream is(dateString);
			is.imbue(INPUT_DATE_FORMAT);
			is >> date;
			// store data point
			this->data.push_back(Point(date, open, high, low, close, volume, adjclose));
//			clog.imbue(DATE_FORMAT);
//			clog << date << ',' << open << ',' << high << ','
//				  << low << ',' << close << ',' << volume << ',' << adjclose << endl;
		} // end while
		csvFile.close();
		reverse(this->data.begin(), this->data.end()); // reverse data
		this->data.push_back(this->data.back()); // add next point
		this->data.back().date += boost::gregorian::days(1);
		this->data.shrink_to_fit();
		computeAll(); // compute all SAR values for the available data
	}
	else
	{
		cerr << "Input file could not be opened." << endl;
	} // end if
} // end readCSV(string filename)

//***************************************************************************
// void writeCSV( string filename, vector<Point> data )
// Purpose: Write historical stock data with SAR and EP values to CSV file
// Output: (file stream) Sequential stock data
//***************************************************************************
void ParabolicSAR::writeCSV(string filename)
{
	ofstream csvFile(filename, ios::out); // open file
	if (csvFile)
	{
		cout << "Write File: \"" << filename << "\"" << endl;
		csvFile << HEADER << endl;
		for (auto it = this->data.begin(); it != this->data.end(); ++it)
		{
			csvFile.imbue(DATE_FORMAT);
			csvFile << (*it).date << DELIMITER << (*it).open << DELIMITER
					  << (*it).high << DELIMITER << (*it).low << DELIMITER
					  << (*it).close << DELIMITER << (*it).volume << DELIMITER
					  << (*it).adjclose << DELIMITER << (*it).sar << DELIMITER
					  << (*it).ep << DELIMITER << (*it).trend << DELIMITER
					  << (*it).af << endl;
		} // end while
		csvFile.close();
	}
	else
	{
		cerr << "Output file could not be opened." << endl;
	} // end if
} // end writeCSV(string filename)

//***************************************************************************
// void printNext( vector<Point> data)
// Purpose: Print SAR values for the latest and next dates
// Output: (std output) Latest date and SAR value
//***************************************************************************
void ParabolicSAR::printSAR()
{
	checkData(); // ensure that sufficient data is available
	Point& next = *(this->data.end()-1);
	Point& current = *(this->data.end()-2);
	cout.imbue(DATE_FORMAT);
	cout << "SAR for " << current.date << ": " << current.sar << endl;
	cout << "SAR for " << next.date << ": " << next.sar << endl;
} // end printNext()

//***************************************************************************
// void checkData( vector<Point> data )
// Purpose: Ensure that sufficient data is available
// Postcondition: Data values are stored in vector
//***************************************************************************
void ParabolicSAR::checkData()
{
	while (this->data.empty() || this->data.size() < MIN_SIZE)
	{
		string filename;
		cout << "Sufficient data required - please enter file name: ";
		cin >> filename;
		readCSV(filename);
	} // end while
} // end checkData()

//***************************************************************************
// void computePoint( Point& current, Point& prev, Point& prevprev )
// Purpose: Compute SAR value for a data Point
// Postcondition: SAR and extreme point values are populated in Point
//***************************************************************************
void ParabolicSAR::computePoint(Point& current, Point& prev, Point& prevprev)
{
	// compute SAR
	current.sar = prev.ep;
	if (prev.trend == prevprev.trend)
	{
		current.sar = prev.sar+prev.af*(prev.ep-prev.sar);
		if (prev.trend == true &&
			 current.sar > min(prevprev.low, prev.low))
		{
			current.sar = min(prevprev.low, prev.low);
		}
		else if (prev.trend == false &&
					current.sar < max(prevprev.high, prev.high))
		{
			current.sar = max(prevprev.high, prev.high);
		} // end if
	} // end if
	// compute EP
	if (prev.trend == true)
	{
		current.ep = max(prev.ep, current.high);
	}
	else
	{
		current.ep = min(prev.ep, current.low);
	} // end if
	// update trend
	current.trend = prev.trend;
	if ((prev.trend == true && current.low < current.sar) ||
		 (prev.trend == false && current.high > current.sar))
	{
		current.trend = !prev.trend;
	} // end if
	// update acceleration factor
	current.af = AF_BASE;
	if (current.trend == prev.trend)
	{
		current.af = prev.af;
		if ((current.trend == true && current.ep > prev.ep) ||
			 (prev.trend == false && current.ep < prev.ep))
		{
			current.af += AF_BASE;
			if (current.af > AF_MAX)
			{
				current.af = AF_MAX;
			} // end if
		} // end if
	} // end if
} // end computePoint(Point& current, Point& prev, Point& prevprev)

//***************************************************************************
// void computeAll( vector<Point> data )
// Purpose: Compute all SAR values for data
// Postcondition: SAR and extreme point values are populated in data vector
//***************************************************************************
void ParabolicSAR::computeAll()
{
	checkData(); // ensure that sufficient data is available
//	this->data.push_back(this->data.back());
	auto it = next(this->data.begin()); // skip first point
	// initialize computed values for second point
	Point& current = *it;
	Point& prev = *(it-1);
	prev.trend = TREND_INIT;
	current.sar = min(prev.low, current.low);
	current.ep = max(prev.high, current.high);
	if (current.low < current.sar)
	{
		current.trend = !current.trend;
	} // end if
	current.af = AF_BASE;
	// compute values
	for (++it; it != this->data.end(); ++it)
	{
		Point& current = *it;
		Point& prev = *(it-1);
		Point& prevprev = *(it-2);
		computePoint(current, prev, prevprev);
	} // end for
} // end computeAll()
