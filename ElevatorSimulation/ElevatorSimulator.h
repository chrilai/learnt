//============================================================================
// ElevatorSimulator.h
// Author: Christine Lai
// Date: November 17, 2014
// Class: 605.404.81 Object-Oriented Programming with C++
// Instructors: Prof. Doug Ferguson, Prof. Hal Pierson
// Assignment: Module 10 - Elevators
// Purpose: Simulate the behavior of elevators in a building
// Inputs: (from input file stream) passenger data values stored in csv file
// Outputs: (to std output) elevator simulation results
//============================================================================
//
#ifndef ELEVATORSIMULATOR_H
#define ELEVATORSIMULATOR_H
#include <array>
#include <deque>
#include "BuildingObjects.h"

class ElevatorSimulator
{
public:
	static const unsigned long int START_TIME = 0; // start time
	static const unsigned int DEFAULT_MOVE_TIME = 10; // movement time
	static const unsigned int STOP_TIME = 2; // stopping time
	static const size_t BOTTOM_FLOOR = 0; // bottom floor
	static const size_t TOP_FLOOR = 99; // top floor
	static const size_t ELEVATORS = 4; // number of elevators
	static const bool STEPPED = false; // smooth movement

	explicit ElevatorSimulator()
		: time(START_TIME), moveTime(DEFAULT_MOVE_TIME), stepped(STEPPED)
	{
	} // end ElevatorSimulator()

	explicit ElevatorSimulator(std::string filename)
		: time(START_TIME), moveTime(DEFAULT_MOVE_TIME), stepped(STEPPED)
	{
		readCSV(filename);
	} // end ElevatorSimulator(std::string filename)

	void setMoveTime(unsigned int moveTime)
	{
		this->moveTime = moveTime;
	} // end setMoveTime(unsigned int moveTime)

	void setStepped(bool stepped)
	{
		this->stepped = stepped;
	} // end setStepped(bool stepped)

	void readCSV(std::string filename); // read passenger data from CSV file
	double getAverageWait() const; // get average wait time
	double getAverageTravel() const; // get average travel time
	double getAverageTime() const; // get average overall time
	void run(); // run simulation
	void printResults() const; // print simulation results
private:
	long int time; // time
	unsigned int moveTime; // elevator movement time
	bool stepped; // elevator movement type
	std::deque<int> targets; // target floors
	std::vector<Passenger> passengers; // passengers
	std::vector<Passenger> passengerArchive; // archived passengers
	std::array<Floor, 1+TOP_FLOOR-BOTTOM_FLOOR> floors; // floors
	std::array<Elevator, ELEVATORS> elevators; // elevators

	bool isComplete() const; // check if simulation is complete
	int getStop(Elevator& elevator) const; // determine next stop
	void addPassenger(Passenger& passenger); // add passenger to floor
	void setTarget(Elevator& elevator); // set elevator target floor
	void moveElevator(Elevator& elevator); // move elevator one step
}; // end class ElevatorSimulation

#endif
