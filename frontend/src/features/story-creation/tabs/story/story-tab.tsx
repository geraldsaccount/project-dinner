import GridLayout from "@/components/layout/grid-layout";
import SectionHeader from "@/components/shared/section-header";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Story } from "@/types/creation";
import { useRef, useState } from "react";
import { useEditorContext } from "../../context/editor-context";

const StoryTab = () => {
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const { story, setStory } = useEditorContext();

  const [bannerPreview, setBannerPreview] = useState(
    story.bannerImage ||
      "https://placehold.co/600x300/e2e8f0/64748b?text=Banner+Preview"
  );

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const previewUrl = URL.createObjectURL(file);
      setBannerPreview(previewUrl);
      setStory((prev) => ({ ...prev, bannerImage: previewUrl }));
    }
  };

  const handleUpdate = (field: keyof Story, value: string) =>
    setStory((prev) => ({ ...prev, [field]: value }));

  return (
    <div className="space-y-4">
      <SectionHeader title="Story Details" />
      <div className="flex flex-col md:flex-row md:items-start md:gap-8">
        <div className="flex-1 min-w-0">
          <Card>
            <CardHeader>
              <CardTitle>Core Information</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <Label htmlFor="story-title">Story Title</Label>
                <Input
                  id="story-title"
                  value={story.title}
                  onChange={(e) => handleUpdate("title", e.target.value)}
                  placeholder=""
                />
              </div>
              <div>
                <Label htmlFor="shop-description">Shop Description</Label>
                <Textarea
                  id="shop-description"
                  value={story.shopDescription}
                  onChange={(e) =>
                    handleUpdate("shopDescription", e.target.value)
                  }
                  placeholder=""
                />
              </div>
            </CardContent>
          </Card>
        </div>
        <div className="md:ml-0 md:mt-0 mt-4 md:flex-shrink-0 md:w-auto">
          <Card>
            <CardHeader>
              <CardTitle>Assets & Visuals</CardTitle>
            </CardHeader>
            <CardContent>
              <Label htmlFor="banner-image">Banner Image</Label>
              <div className="mt-2 items-center space-y-2">
                <img
                  className="w-48 h-24 object-cover rounded-md"
                  src={bannerPreview}
                  alt="Banner Preview"
                />
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleFileChange}
                  className="hidden"
                  ref={fileInputRef}
                />
                <Button
                  type="button"
                  onClick={() =>
                    fileInputRef.current && fileInputRef.current.click()
                  }
                >
                  Upload Banner
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
      <GridLayout gridCols={{ base: 1, md: 2 }}>
        <Card>
          <CardHeader>
            <CardTitle>Game Rules</CardTitle>
          </CardHeader>
          <CardContent>
            <Textarea
              id="rules"
              value={story.rules}
              onChange={(e) => handleUpdate("rules", e.target.value)}
              rows={10}
              placeholder=""
            />
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Setting & Background</CardTitle>
          </CardHeader>
          <CardContent>
            <Textarea
              id="setting"
              value={story.setting}
              onChange={(e) => handleUpdate("setting", e.target.value)}
              rows={10}
              placeholder=""
            />
          </CardContent>
        </Card>
      </GridLayout>
    </div>
  );
};

export default StoryTab;
