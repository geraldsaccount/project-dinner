import GridLayout from "@/components/layout/grid-layout";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Link } from "react-router-dom";

const HomePage = () => {
  return (
    <div className="space-y-4">
      <section className="w-full text-center mb-8 p-6 border-b-4 border-primary bg-primary text-primary-foreground">
        <h1 className="text-5xl sm:text-6xl md:text-8xl font-extrabold uppercase tracking-tighter mb-4 leading-none">
          Killinary
        </h1>
        <p className="text-xl md:text-3xl font-bold uppercase tracking-wide">
          The app for killer dinner parties
        </p>
      </section>

      <GridLayout gridCols={{ base: 1, md: 2 }}>
        <Card>
          <CardHeader>
            <CardTitle className="text-center">Host a masterpiece</CardTitle>
          </CardHeader>
          <CardContent className="text-center flex flex-col items-center">
            <p className="text-base leading-relaxed mb-6 flex-grow">
              Become the ultimate host. Create unique murder mystery scenarios,
              manage guests, and guide the narrative from start to finish. All
              the tools you need for a killer evening.
            </p>
            <Link to={"/dinners/host"}>
              <Button>Start hosting</Button>
            </Link>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle className="text-center">Solve the crime</CardTitle>
          </CardHeader>
          <CardContent className="text-center flex flex-col items-center">
            <p className="text-base leading-relaxed mb-6 flex-grow">
              Step into a new persona. Immerse yourself in a thrilling mystery,
              gather clues, interrogate suspects, and unmask the culprit. Your
              detective skills will be put to the test.
            </p>
            <Link to="invite">
              <Button>Join a dinner</Button>
            </Link>
          </CardContent>
        </Card>
      </GridLayout>

      <section className="w-full p-6 border-2 border-black bg-neutral-900 text-white text-center">
        <h2 className="text-3xl font-bold uppercase mb-4">
          READY TO MURDER YOUR FRIENDS... <br />
          in game
        </h2>
        <p className="text-lg leading-relaxed mb-6">
          Join Killinary today and transform your dinner parties into
          unforgettable nights of intrigue!
        </p>
        <Link to="/dinners/host">
          <Button variant={"secondary"}>GET STARTED NOW</Button>
        </Link>
      </section>
    </div>
  );
};

export default HomePage;
