//============================================================================
// ElevatorSimulation.h
// Author: Christine Lai
// Date: November 17, 2014
// Class: 605.404.81 Object-Oriented Programming with C++
// Instructors: Prof. Doug Ferguson, Prof. Hal Pierson
// Assignment: Module 10 - Elevators
// Purpose: Simulate the behavior of elevators in a building
// Inputs: (from csv ifstream) passenger data values
// Outputs: (to std output) elevator simulation results
//============================================================================
//
#ifndef ELEVATORSIMULATION_H
#define ELEVATORSIMULATION_H
#include <array>
#include <list>
#include "BuildingObjects.h"

class ElevatorSimulation
{
public:
	static const unsigned long int START_TIME = 0;
	static const unsigned int DEFAULT_MOVE_TIME = 10;
	static const unsigned int STOP_TIME = 2;
	static const size_t BOTTOM_FLOOR = 0;
	static const size_t TOP_FLOOR = 99;
	static const size_t ELEVATORS = 4;
	static const bool CONTINUOUS = true;

	explicit ElevatorSimulation()
		: time(START_TIME), moveTime(DEFAULT_MOVE_TIME), continuous(CONTINUOUS)
	{
	} // end ElevatorSimulation()

	explicit ElevatorSimulation(std::string filename)
		: time(START_TIME), moveTime(DEFAULT_MOVE_TIME), continuous(CONTINUOUS)
	{
		readCSV(filename);
	} // end ElevatorSimulation(std::string filename)

	void setMoveTime(unsigned int moveTime)
	{
		this->moveTime = moveTime;
	} // end setMoveTime(unsigned int moveTime)

	void setContinuous(bool continuous)
	{
		this->continuous = continuous;
	} // end setStepping(bool stepping)

	void readCSV(std::string filename); // read passenger data from CSV file
	double getAverageWait() const; // get average wait time
	double getAverageTravel() const; // get average travel time
	double getAverageTime() const; // get average travel time
	void run(); // run simulation
	void reset(); // reset simulation
	void printResults() const; // print simulation results
private:
	long int time; // time
	unsigned int moveTime; // elevator movement speed
	bool continuous; // elevator movement type
	std::list<Passenger> passengers; // passengers
	std::vector<Passenger> passengerArchive; // archived passengers
//	std::array<Floor, 1+TOP_FLOOR-BOTTOM_FLOOR> floors; // floors
	std::array<Elevator, ELEVATORS> elevators; // elevators
	bool isOccupied(Floor floor) const;
	Passenger getPassenger(Floor floor) const;
	void removePassenger(Floor floor) const;

	bool isComplete() const; // check if simulation is complete
	bool isStop(Elevator& elevator) const; // check current floor
	Floor getStop(Elevator& elevator) const; // determine next stop
	void addPassenger(Passenger& passenger); // add passenger to floors
	void setTarget(Elevator& elevator); // reset direction
	void moveElevator(Elevator& elevator); // move elevator one step
}; // end class ElevatorSimulation

#endif
