import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Textarea } from "@/components/ui/textarea";
import { useEditorContext } from "../../context/editor-context";
import SectionHeader from "@/components/shared/section-header";
import MultiCharacterPicker from "@/components/shared/multi-character-picker";

const CrimeTab = () => {
  const { characters, crime, setCrime } = useEditorContext();
  const selectedCriminalIds = crime.criminalIds;

  return (
    <div className="space-y-4">
      <SectionHeader title="The Crime" />
      <Card>
        <CardHeader>
          <CardTitle>The Culprit(s)</CardTitle>
          <CardDescription>
            The murderer(s) are fixed across all player configurations.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <MultiCharacterPicker
            characters={characters}
            selectedIds={selectedCriminalIds}
            onChange={(ids) =>
              setCrime((prev) => ({ ...prev, criminalIds: ids }))
            }
            placeholder="Select culprit(s)"
            buttonClassName="justify-start"
            disabled={characters.length === 0}
          />
        </CardContent>
      </Card>
      <Card>
        <CardHeader>
          <CardTitle>The Solution</CardTitle>
          <CardDescription>
            How the crime was committed. This is the final reveal.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Textarea
            rows={12}
            placeholder="Lady Eleanor, fueled by a secret vendetta..."
            value={crime.description}
            onChange={(desc) =>
              setCrime((prev) => ({ ...prev, description: desc.target.value }))
            }
          />
        </CardContent>
      </Card>
    </div>
  );
};

export default CrimeTab;
