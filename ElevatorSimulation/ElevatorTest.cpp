// ElevatorTest.cpp
//
#include <cmath>
#include <iomanip>
#include <iostream>
#include "ElevatorSimulator.h"
using namespace std;

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
	}
	return testResult;
}

//***************************************************************************
// void test( Tree t, Tree dt )
// Purpose: Perform unit testing on expression tree
//***************************************************************************
void test()
{
	cout << "Object Unit Testing" << '\n' << endl;

	// Passenger
	Passenger passenger = Passenger(0, 1, 2);
	cout << "Passenger: " << passenger.toString() << '\n';
	cout << "Passenger ascending: " << boolalpha << passenger.isAscending() << '\n';
	cout << testResult(true, passenger.isAscending()) << endl;

	// Floor
	Floor floor = Floor();
	floor.addPassenger(passenger);
	cout << "Floor passenger added: " << passenger.toString() << '\n';
	cout << "Floor passengers: " << floor.numPassengers() << '\n';
	cout << testResult(1, floor.numPassengers()) << endl;
	cout << "Floor empty: " << boolalpha << floor.isEmpty() << '\n';
	cout << testResult(false, floor.isEmpty()) << endl;
	floor.removePassenger();
	cout << "Floor passenger removed: " << passenger.toString() << '\n';
	cout << "Floor passengers: " << floor.numPassengers() << '\n';
	cout << testResult(0, floor.numPassengers()) << endl;
	cout << "Floor empty: " << boolalpha << floor.isEmpty() << '\n';
	cout << testResult(true, floor.isEmpty()) << endl;

	// Elevator
	Elevator elevator = Elevator();
	elevator.addPassenger(passenger, 3);
	cout << "Elevator passenger added?" << '\n';
	cout << "Elevator floor: " << elevator.getFloor() << '\n';
	cout << "Elevator passengers: " << elevator.numPassengers() << '\n';
	cout << testResult(0, elevator.numPassengers()) << endl;
	cout << "Elevator empty: " << boolalpha << elevator.isEmpty() << '\n';
	cout << testResult(true, elevator.isEmpty()) << endl;
	cout << "Elevator full: " << boolalpha << elevator.isFull() << '\n';
	cout << testResult(false, elevator.isFull()) << endl;
	elevator.setFloor(1);
	elevator.addPassenger(passenger, 3);
	cout << "Elevator passenger added?" << '\n';
	cout << "Elevator floor: " << elevator.getFloor() << '\n';
	cout << "Elevator passengers: " << elevator.numPassengers() << '\n';
	cout << testResult(1, elevator.numPassengers()) << endl;
	cout << "Elevator empty: " << boolalpha << elevator.isEmpty() << '\n';
	cout << testResult(false, elevator.isEmpty()) << endl;
	cout << "Elevator full: " << boolalpha << elevator.isFull() << '\n';
	cout << testResult(false, elevator.isFull()) << endl;
	elevator.removePassenger();
	cout << "Elevator passenger removed?" << '\n';
	cout << "Elevator floor: " << elevator.getFloor() << '\n';
	cout << "Elevator passengers: " << elevator.numPassengers() << '\n';
	cout << testResult(1, elevator.numPassengers()) << endl;
	cout << "Elevator empty: " << boolalpha << elevator.isEmpty() << '\n';
	cout << testResult(false, elevator.isEmpty()) << endl;
	cout << "Elevator full: " << boolalpha << elevator.isFull() << '\n';
	cout << testResult(false, elevator.isFull()) << endl;
	elevator.setFloor(2);
	elevator.removePassenger();
	cout << "Elevator passenger removed?" << '\n';
	cout << "Elevator floor: " << elevator.getFloor() << '\n';
	cout << "Elevator passengers: " << elevator.numPassengers() << '\n';
	cout << testResult(0, elevator.numPassengers()) << endl;
	cout << "Elevator empty: " << boolalpha << elevator.isEmpty() << '\n';
	cout << testResult(true, elevator.isEmpty()) << endl;
	cout << "Elevator full: " << boolalpha << elevator.isFull() << '\n';
	cout << testResult(false, elevator.isFull()) << endl;

	cout << endl << endl;
}

int main()
{
	const string PASSENGER_DATA = "Elevators.csv";

	// Perform Unit Testing
	test();

	// Demonstrate Assignment Example
	cout << "Elevator Simulator" << '\n' << endl;

	// Stepped Movement
	cout << "Stepped Movement" << '\n';
	ElevatorSimulator slowSim = ElevatorSimulator(PASSENGER_DATA);
	slowSim.setMoveTime(1);
	slowSim.setStepped(true);
	slowSim.run();
	slowSim.printResults();

	// Smooth Movement, Speed = 10
	cout << "Smooth Movement, Speed = 10" << '\n';
	ElevatorSimulator sim = ElevatorSimulator(PASSENGER_DATA);
	sim.run();
	sim.printResults();
	cout << "Average Wait Time Reduction: " << setprecision(4)
		  << 100*abs(sim.getAverageWait()-slowSim.getAverageWait())/slowSim.getAverageWait()
		  << " %" << '\n';
	cout << "Average Travel Time Reduction: "
		  << 100*abs(sim.getAverageTravel()-slowSim.getAverageTravel())/slowSim.getAverageTravel()
		  << " %" << '\n';
	cout << "Average Overall Time Reduction: "
		  << 100*abs(sim.getAverageTime()-slowSim.getAverageTime())/slowSim.getAverageTime()
		  << " %" << '\n';
	cout << endl;

	// Smooth Movement, Speed = 5
	cout << "Smooth Movement, Speed = 5" << '\n';
	ElevatorSimulator fastSim = ElevatorSimulator(PASSENGER_DATA);
	fastSim.setMoveTime(5);
	fastSim.run();
	fastSim.printResults();
	cout << "Average Wait Time Reduction: "
		  << 100*abs(sim.getAverageWait()-fastSim.getAverageWait())/sim.getAverageWait()
		  << " %" << '\n';
	cout << "Average Travel Time Reduction: "
		  << 100*abs(sim.getAverageTravel()-fastSim.getAverageTravel())/sim.getAverageTravel()
		  << " %" << '\n';
	cout << "Average Overall Time Reduction: "
		  << 100*abs(sim.getAverageTime()-fastSim.getAverageTime())/sim.getAverageTime()
		  << " %" << '\n';
	cout << endl;

	return 0;
}
