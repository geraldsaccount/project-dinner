import CharacterAvatar from "@/components/shared/character-avatar";
import SectionHeader from "@/components/shared/section-header";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from "@/components/ui/card";
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { CharacterDetailDto, PrivateCharacterInfo } from "@/types";
import { Eye, EyeClosed } from "lucide-react";
import { useState } from "react";

type Props = {
  character: CharacterDetailDto;
  secret: PrivateCharacterInfo;
};

const DinnerSecret = ({ character, secret }: Props) => {
  const [isHidden, setIsHidden] = useState(false);

  const toggleHidden = () => {
    setIsHidden(!isHidden);
  };

  return (
    <section className="flex flex-col gap-4">
      <div className="flex justify-between items-center">
        <SectionHeader title="Your Private Information" />
        <Tooltip>
          <TooltipTrigger asChild>
            <Button onClick={toggleHidden} variant="outline">
              {isHidden ? <EyeClosed /> : <Eye />}
            </Button>
          </TooltipTrigger>
          <TooltipContent>
            <p>
              {isHidden ? "Show Secret Information" : "Hide Secret Information"}
            </p>
          </TooltipContent>
        </Tooltip>
      </div>
      {isHidden ? (
        <button className="cursor-pointer" onClick={toggleHidden}>
          <Card>
            <CardContent className="blur-xs">
              <div className="flex items-center mb-4">
                <div className="w-20 h-20 rounded-full bg-gray-300 mr-4"></div>
                <div className="flex-1 space-y-3">
                  <div className="h-6 bg-gray-300 rounded w-3/4"></div>
                  <div className="h-4 bg-gray-300 rounded w-1/2"></div>
                </div>
              </div>
              <div className="redacted-placeholder">
                <div className="space-y-3">
                  <div className="h-4 bg-gray-300 rounded"></div>
                  <div className="h-4 bg-gray-300 rounded"></div>
                  <div className="h-4 bg-gray-300 rounded w-5/6"></div>
                  <div className="h-4 bg-gray-300 rounded"></div>
                </div>
              </div>
            </CardContent>
            <CardFooter className="flex justify-around">
              <p className="text-center mt-4 font-semibold text-red-700">
                Content hidden. Click to reveal your secret briefing.
              </p>
            </CardFooter>
          </Card>
        </button>
      ) : (
        <Card>
          <CardHeader className="flex items-center">
            <CharacterAvatar character={character} className="w-20 h-20 mr-4" />
            <div>
              <h4 className="text-xl font-bold">
                Your Character: {character.name}
              </h4>
              <p className="text-sm text-muted-foreground">{character.role}</p>
            </div>
          </CardHeader>
          <CardContent className="space-y-4">
            {secret.privateDescription
              .split(/\r?\n/)
              .filter(Boolean)
              .map((line, idx) => (
                <p key={idx}>{line}</p>
              ))}
          </CardContent>
        </Card>
      )}
    </section>
  );
};

export default DinnerSecret;
