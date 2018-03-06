//============================================================================
// ParabolicSAR.h
// Author: Christine Lai
// Date: December 15, 2014
// Class: 605.404.81 Object-Oriented Programming with C++
// Instructors: Prof. Doug Ferguson, Prof. Hal Pierson
// Assignment: Module 12 - Parabolic SAR
// Purpose: Calculate the parabolic stop and reverse (SAR) for stock prices
// Inputs: (from input file stream) Historical price data
// Outputs: (to std output) Parabolic SAR results
//============================================================================
#ifndef PARABOLICSAR_H
#define PARABOLICSAR_H
#include <vector>
#include <boost/date_time/gregorian/gregorian.hpp>

class ParabolicSAR
{
public:
	struct Point
	{
		boost::gregorian::date date; // historical date
		double open; // opening price
		double high; // high price
		double low; // low price
		double close; // closing price
		int volume; // daily volume
		double adjclose; // adjusted closing price
		double sar; // stop and reverse (SAR)
		double ep; // extreme point (EP)
		bool trend; // trend indicator (false:downtrend,true:uptrend)
		double af; // acceleration factor
		Point(boost::gregorian::date date, double open, double high, double low,
				double close, int volume, double adjclose) // constructor
			: date(date), open(open), high(high), low(low), close(close),
			  volume(volume), adjclose(adjclose), sar(0), ep(0), trend(0), af(0)
		{
		}
	}; // end struct Point

	static const size_t MIN_SIZE = 5; // minimum data points required
	static constexpr double AF_MAX = 0.2; // maximum acceleration factor
	static constexpr double AF_BASE = 0.02; // base acceleration factor
	static constexpr double TREND_INIT = true; // initial trend indicator
	static const std::string DELIMITER; // delimiter
	static const std::string HEADER; // header row
	static const boost::char_separator<char> SEPARATOR; // separator
	static const std::locale DATE_FORMAT; // date format
	static const std::locale INPUT_DATE_FORMAT; // input date format

	explicit ParabolicSAR() // constructor
	{
	} // end ParabolicSAR()

	void printSAR(); // print latest and next SAR values
	void readCSV(std::string filename); // read data from CSV file
	void writeCSV(std::string filename); // write data to CSV file
private:
	std::vector<Point> data; // data points

	void checkData(); // ensure sufficient data
	void computePoint(Point& current, Point& prev, Point& prevprev); // compute SAR value
	void computeAll(); // compute all SAR values
}; // end class ParabolicSAR

#endif
